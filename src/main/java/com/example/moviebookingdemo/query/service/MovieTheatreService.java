package com.example.moviebookingdemo.query.service;

import com.example.moviebookingdemo.command.aggregate.entity.Booking;
import com.example.moviebookingdemo.command.events.*;
import com.example.moviebookingdemo.coreapi.CommonUtils;
import com.example.moviebookingdemo.coreapi.MovieSlot;
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
                        .capacity(event.getNumOfSeats())
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
        existingMovieTheatreDTO.setCurrentMovie(CommonUtils.selectRandomMovie(movies));
        existingMovieTheatreDTO.setMovies(movies);

    }

    @EventHandler
    public void on(MovieTheatreDeletedEvent event) throws InvalidOperationException{
        String movieTheatreId = event.getId();
        if(null == availableMovieTheatres.get(movieTheatreId)) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        availableMovieTheatres.remove(movieTheatreId);
    }

    @EventHandler
    public void on(BookMovieEvent event) throws InvalidOperationException{
        String movieTheatreId = event.getMovieTheatreId();


        MovieTheatreDTO movieTheatreDTO = availableMovieTheatres.get(movieTheatreId);
        if(null == movieTheatreDTO) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        int capacity = movieTheatreDTO.getCapacity();
        Map<String,MovieSlot> allMovies = movieTheatreDTO.getMovies();
        Map<String,List<Booking>> allMovieBookings = movieTheatreDTO.getBookings();

        if(allMovies.size()==0) throw new InvalidOperationException(EMPTY_MOVIE_THEATRE);

        if(null==allMovies.get(event.getMovieName())) throw new InvalidOperationException(INVALID_MOVIE);

        if(!allMovies.get(event.getMovieName()).equals(event.getMovieSlot())) throw new InvalidOperationException(INVALID_MOVIE_SLOT);

        if(event.getNumberOfSeats()>capacity) throw new InvalidOperationException(INVALID_BOOKING_CNT_MAX);

        if(event.getNumberOfSeats()>MAX_BOOKINGS_IN_TRANSACTION) throw new InvalidOperationException(INVALID_BOOKING_CNT_THRESHOLD);

        //ToDo: use map's computeifAbsent Apis
        List<Booking> currentMovieBookings = new ArrayList<>();

        if(allMovieBookings.size()>0 && null!=allMovieBookings.get(event.getMovieName())) currentMovieBookings = allMovieBookings.get(event.getMovieName());

        if(currentMovieBookings.size()>0){
            int availableBookings = capacity-currentMovieBookings.size();

            if(availableBookings < event.getNumberOfSeats()) throw new InvalidOperationException(INVALID_BOOKING_CNT_NOT_ENOUGH_AVAIL);
        }

        Booking booking  = Booking.builder()
                                    .id(event.getId())
                                    .userId(event.getUserId())
                                    .movieTheatreId(movieTheatreId)
                                    .movieName(event.getMovieName())
                                    .movieSlot(event.getMovieSlot())
                                    .numOfSeatsBooked(event.getNumberOfSeats())
                                    .date(LocalDateTime.now().withSecond(0).withNano(0))  //<--   To avoid aggregate illegal state exception
                                .build();

        if(currentMovieBookings.contains(booking)) throw new InvalidOperationException(INVALID_BOOKING_STATE_ALREADY_EXISTS);

        currentMovieBookings.add(booking);

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


    public List<MovieTheatreDTO> getAllMovieTheatres(){
         return new ArrayList<>(availableMovieTheatres.values());
    }

    public List<MovieTheatreDTO> getAllAvailableMovieSlots(){
        return new ArrayList<>(availableMovieTheatres.values());
    }

    public List<CurrentlyScreenedMovieDTO> getAllCurrentlyScreenedMovies(){

        return availableMovieTheatres.values()
                .stream()
                .map(mt -> modelMapper.map(mt, CurrentlyScreenedMovieDTO.class))
                .collect(Collectors.toList());
    }


}
