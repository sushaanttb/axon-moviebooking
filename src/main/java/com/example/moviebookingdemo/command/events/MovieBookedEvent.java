package com.example.moviebookingdemo.command.events;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class MovieBookedEvent {

    String movieTheatreId;

    private String id;

    String userName;

    String movieName;

    MovieSlot movieSlot;

    int numberOfSeats;

    LocalDateTime date;
}
