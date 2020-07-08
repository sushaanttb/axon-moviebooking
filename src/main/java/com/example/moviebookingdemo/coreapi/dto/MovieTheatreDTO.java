package com.example.moviebookingdemo.coreapi.dto;

import com.example.moviebookingdemo.command.aggregate.entity.Booking;
import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MovieTheatreDTO {

    private String id;

    private String name;

    private int capacity;

    private String currentMovie;

    Map<String, MovieSlot> movies;

    Map<String, List<Booking>> bookings;

}
