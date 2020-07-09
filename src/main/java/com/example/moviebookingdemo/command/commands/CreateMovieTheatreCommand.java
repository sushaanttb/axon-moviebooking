package com.example.moviebookingdemo.command.commands;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Map;

@Data
@Builder
public class CreateMovieTheatreCommand {

    @TargetAggregateIdentifier
    private String id;

    private String name;

    private int capacity;

    //<movieName,timings>
    Map<String, MovieSlot> movies;

}
