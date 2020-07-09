package com.example.moviebookingdemo.query.service;

import com.example.moviebookingdemo.command.aggregate.entity.Booking;
import com.example.moviebookingdemo.command.events.*;
import com.example.moviebookingdemo.coreapi.CommonUtils;
import com.example.moviebookingdemo.coreapi.MovieSlot;
import com.example.moviebookingdemo.coreapi.dto.BookingDTO;
import com.example.moviebookingdemo.coreapi.dto.MovieTheatreDTO;
import com.example.moviebookingdemo.coreapi.exception.InvalidOperationException;
import com.example.moviebookingdemo.query.projections.CurrentlyScreenedMovieDTO;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.moviebookingdemo.coreapi.Constants.*;

@Service
public class MovieTheatreService {

    /*
        This is our in-memory MovieTheatreRepository
        Also, avoiding dumping the aggregate object as not sure on the impact on the Lifecycle
        hence saving in form of DTO
    */
    private Map<String, MovieTheatreDTO> availableMovieTheatres = new HashMap<>();

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventGateway eventGateway;

    @EventHandler
    public void on(MovieTheatreCreatedEvent event){
        String movieTheatreId = event.getId();
        Map<String,MovieSlot> movies = event.getMovies();

        availableMovieTheatres.put(movieTheatreId,
                MovieTheatreDTO
                        .builder()
                        .id(movieTheatreId)
                        .name(event.getName())
                        .capacity(event.getCapacity())
                        .currentMovie(CommonUtils.selectRandomMovie(movies))
                        .movies(movies)
                        .bookings(new HashMap<>())
                        .build()
        );
    }

    @EventHandler
    public void on(MovieTheatreUpdatedEvent event) throws InvalidOperationException{
        String movieTheatreId = event.getId();
        Map<String,MovieSlot> movies = event.getMovies();
        MovieTheatreDTO existingMovieTheatreDTO = availableMovieTheatres.get(movieTheatreId);

        if(null == existingMovieTheatreDTO) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        existingMovieTheatreDTO.setName(event.getName());
        existingMovieTheatreDTO.setCapacity(event.getCapacity());
        existingMovieTheatreDTO.setCurrentMovie(CommonUtils.selectRandomMovie(movies));
        existingMovieTheatreDTO.setMovies(movies);

    }

    @EventHandler
    public void on(MovieTheatreDeletedEvent event) throws InvalidOperationException{
        String movieTheatreId = event.getId();
        if(null == availableMovieTheatres.get(movieTheatreId)) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        availableMovieTheatres.remove(movieTheatreId);
    }

    //This event is generated from MovieTheatreAggregate post handling the basic validations
    @EventHandler
    public void on(BookMovieEvent event) throws InvalidOperationException{
        String movieTheatreId = event.getMovieTheatreId();

        MovieTheatreDTO movieTheatreDTO = availableMovieTheatres.get(movieTheatreId);

        //Basic MovieTheatre Booking Validations(based on bookings)
        if(null == movieTheatreDTO) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        Map<String,List<BookingDTO>> allMovieBookings = movieTheatreDTO.getBookings();
        if(null==allMovieBookings) allMovieBookings = new HashMap<>();

        List<BookingDTO> currentMovieBookings = allMovieBookings.get(event.getMovieName());
        if(null!=currentMovieBookings) currentMovieBookings = new ArrayList<>();

        if(currentMovieBookings.size()>0){
            int availableBookings = movieTheatreDTO.getCapacity()-currentMovieBookings.size();

            if(availableBookings < event.getNumberOfSeats()) throw new InvalidOperationException(INVALID_BOOKING_CNT_NOT_ENOUGH_AVAIL);
        }

        BookingDTO bookingDTO  = BookingDTO.builder()
                                    .id(event.getId())
                                    .userId(event.getUserId())
                                    .movieTheatreId(movieTheatreId)
                                    .movieName(event.getMovieName())
                                    .movieSlot(event.getMovieSlot())
                                    .numOfSeatsBooked(event.getNumberOfSeats())
                                    .date(LocalDateTime.now().withSecond(0).withNano(0))  //<--   To avoid aggregate illegal state exception
                                .build();

        if(currentMovieBookings.contains(bookingDTO)) throw new InvalidOperationException(INVALID_BOOKING_STATE_ALREADY_EXISTS);

        currentMovieBookings.add(bookingDTO);
        allMovieBookings.put(event.getMovieName(), currentMovieBookings);
        movieTheatreDTO.setBookings(allMovieBookings);
        availableMovieTheatres.put(movieTheatreId,movieTheatreDTO);

        eventGateway.publish(MovieBookedEvent.builder()
                                                .id(event.getId())
                                                .userId(event.getUserId())
                                                .movieTheatreId(event.getMovieTheatreId())
                                                .movieName(event.getMovieName())
                                                .movieSlot(event.getMovieSlot())
                                                .numberOfSeats(event.getNumberOfSeats())
                                            .build()
        );
    }


    public List<CurrentlyScreenedMovieDTO> getAllCurrentlyScreenedMovies(){

        return availableMovieTheatres.values()
                .stream()
                .map(mt -> modelMapper.map(mt, CurrentlyScreenedMovieDTO.class))
                .collect(Collectors.toList());
    }

    public List<MovieTheatreDTO> getAllMovieTheatres(){
        return new ArrayList<>(availableMovieTheatres.values());
    }

    public List<MovieTheatreDTO> getAllAvailableMovieSlots(){
        return new ArrayList<>(availableMovieTheatres.values());
    }

}
