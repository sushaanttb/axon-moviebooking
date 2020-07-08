package com.example.moviebookingdemo.command.events;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookMovieEvent {
    String movieTheatreId;

    private String id;

    String userId;

    String movieName;

    MovieSlot movieSlot;

    int numberOfSeats;
}
