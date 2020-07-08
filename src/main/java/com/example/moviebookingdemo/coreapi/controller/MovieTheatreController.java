package com.example.moviebookingdemo.coreapi.controller;

import com.example.moviebookingdemo.command.commands.*;
import com.example.moviebookingdemo.coreapi.dto.BookingDTO;
import com.example.moviebookingdemo.coreapi.dto.MovieTheatreDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
        String movieTheatreId = UUID.randomUUID().toString();
        commandGateway.send(
                CreateMovieTheatreCommand.builder()
                        .id(movieTheatreId)
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
    }


}
