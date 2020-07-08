package com.example.moviebookingdemo.command.events;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MovieTheatreUpdatedEvent {

    private String id;

    private String name;

    private int numOfSeats;

    Map<String, MovieSlot> movies;
}
