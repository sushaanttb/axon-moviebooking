package com.example.moviebookingdemo.command.events;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MovieBookedEvent {

    String movieTheatreId;

    private String id;

    String userId;

    String movieName;

    MovieSlot movieSlot;

    int numberOfSeats;
}
