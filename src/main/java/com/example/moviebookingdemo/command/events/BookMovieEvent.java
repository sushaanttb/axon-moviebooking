package com.example.moviebookingdemo.command.events;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookMovieEvent {

    String movieTheatreId;

    private String id;

    String userName;

    String movieName;

    MovieSlot movieSlot;

    int numberOfSeats;

    LocalDateTime date;
}
