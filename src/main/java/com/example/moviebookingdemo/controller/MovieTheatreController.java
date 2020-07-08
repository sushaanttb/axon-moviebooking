package com.example.moviebookingdemo.controller;

import com.example.moviebookingdemo.command.commands.*;
import com.example.moviebookingdemo.command.events.MovieBookedEvent;
import com.example.moviebookingdemo.coreapi.dto.BookingDTO;
import com.example.moviebookingdemo.coreapi.dto.MovieTheatreDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/movie-theatre")
public class MovieTheatreController {

    @Autowired
    private CommandGateway commandGateway;

//    Didn't worked as expected
//    @Autowired
//    private EventGateway eventGateway;

    @PostMapping
    public void createMovieTheatre(@RequestBody MovieTheatreDTO movieTheatreDTO){
        commandGateway.send(
                CreateMovieTheatreCommand.builder()
                        .id(UUID.randomUUID().toString())
                        .name(movieTheatreDTO.getName())
                        .numOfSeats(movieTheatreDTO.getNumOfSeats())
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
                        .numOfSeats(movieTheatreDTO.getNumOfSeats())
                        .movies(movieTheatreDTO.getMovies())
                        .build()
        );
    }

    @DeleteMapping
    public void deleteMovieTheatre(@RequestParam(value="id") String movieTheatreId){
        commandGateway.send(new DeleteMovieTheatreCommand(movieTheatreId));
    }


    @PostMapping("/book")
    public void book(@RequestBody BookingDTO bookingDTO){
        String bookingId = UUID.randomUUID().toString();

        CompletableFuture<Object> future =
                commandGateway.send(
                BookMovieCommand.builder()
                            .id(bookingId)
                            .userId(bookingDTO.getUserId())
                            .movieTheatreId(bookingDTO.getMovieTheatreId())
                            .movieName(bookingDTO.getMovieName())
                            .movieSlot(bookingDTO.getMovieSlot())
                            .numberOfSeats(bookingDTO.getNumOfSeatsBooked())
                            .build()
        );

        future.thenAcceptAsync(o ->

            commandGateway.send(
                    CreateTicketCommand.builder()
                            .id(UUID.randomUUID().toString())
                            .userId(bookingDTO.getUserId())
                            .movieTheatreId(bookingDTO.getMovieTheatreId())
                            .movieName(bookingDTO.getMovieName())
                            .movieSlot(bookingDTO.getMovieSlot())
                            .numberOfSeats(bookingDTO.getNumOfSeatsBooked())
                            .build()
            )
        );
    }


}
