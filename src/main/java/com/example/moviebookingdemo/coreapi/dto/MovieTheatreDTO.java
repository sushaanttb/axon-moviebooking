package com.example.moviebookingdemo.coreapi.dto;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Data;

import java.util.Map;

@Data
public class MovieTheatreDTO {

    private String id;

    private String name;

    private int numOfSeats;

    Map<String, MovieSlot> movies;
}
