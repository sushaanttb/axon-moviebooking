package com.example.moviebookingdemo.coreapi.controller;

import com.example.moviebookingdemo.command.commands.*;
import com.example.moviebookingdemo.coreapi.dto.BookingDTO;
import com.example.moviebookingdemo.coreapi.dto.MovieTheatreDTO;
import com.example.moviebookingdemo.query.projections.CurrentlyScreenedMovieDTO;
import com.example.moviebookingdemo.query.queries.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/movie-theatre")
public class MovieTheatreController {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private QueryGateway queryGateway;


    @PostMapping
    public void createMovieTheatre(@RequestBody MovieTheatreDTO movieTheatreDTO){
        String movieTheatreId = UUID.randomUUID().toString();
        commandGateway.send(
                CreateMovieTheatreCommand.builder()
                        .id(movieTheatreId)
                        .name(movieTheatreDTO.getName())
                        .capacity(movieTheatreDTO.getCapacity())
                        .movies(movieTheatreDTO.getMovies())
                        .build()
                );
    }

    @PutMapping
    public void updateMovieTheatre(@RequestBody MovieTheatreDTO movieTheatreDTO){
        commandGateway.send(
                UpdateMovieTheatreCommand.builder()
                        .id(movieTheatreDTO.getId())
                        .name(movieTheatreDTO.getName())
                        .capacity(movieTheatreDTO.getCapacity())
                        .movies(movieTheatreDTO.getMovies())
                        .build()
        );
    }

    @DeleteMapping
    public void deleteMovieTheatre(@RequestParam(value="id") String movieTheatreId){
        commandGateway.send(new DeleteMovieTheatreCommand(movieTheatreId));
    }


    @PostMapping("/book")
    public BookingDTO book(@RequestBody BookingDTO bookingDTO){
        String bookingId = UUID.randomUUID().toString();

        String responseAggregateId = commandGateway.sendAndWait(
                BookMovieCommand.builder()
                            .id(bookingId)
                            .userName(bookingDTO.getUserName())
                            .movieTheatreId(bookingDTO.getMovieTheatreId())
                            .movieName(bookingDTO.getMovieName())
                            .movieSlot(bookingDTO.getMovieSlot())
                            .numberOfSeats(bookingDTO.getNumOfSeatsBooked())
                            .date(LocalDateTime.now().withSecond(0).withNano(0))
                            .build()
        );


        return  queryGateway.query(
                        GetBookingQuery.builder()
                            .bookingId(bookingId)
                            .movieName(bookingDTO.getMovieName())
                            .movieTheatreId(bookingDTO.getMovieTheatreId())
                        .build(),
                        ResponseTypes.instanceOf(BookingDTO.class)
                ).join();

    }


    @GetMapping("/all")
    public List<MovieTheatreDTO> getAllMovieTheatres(){
        return queryGateway.query(
                AllMovieTheatresQuery.builder().build(),
                ResponseTypes.multipleInstancesOf(MovieTheatreDTO.class)
        ).join();
    }

    @GetMapping("/empty")
    public List<MovieTheatreDTO> getAllEmptyMovieTheatres(){
        return queryGateway.query(
                AllEmptyMovieTheatreMoviesQuery.builder().build(),
                ResponseTypes.multipleInstancesOf(MovieTheatreDTO.class)
        ).join();
    }

    @GetMapping("/movie-slot/all")
    public List<MovieTheatreDTO> getAllTheatresMovieSlots(){
        return queryGateway.query(
                AvailableMovieSlotsQuery.builder().build(),
                ResponseTypes.multipleInstancesOf(MovieTheatreDTO.class)
        ).join();
    }

    @GetMapping("/current-movie/all")
    public List<CurrentlyScreenedMovieDTO> getAllCurrentlyScreenedMovies(){
        return queryGateway.query(
                CurrentlyScreenedMoviesQuery.builder().build(),
                ResponseTypes.multipleInstancesOf(CurrentlyScreenedMovieDTO.class)
        ).join();
    }

}
