package com.example.moviebookingdemo.command.commands;


import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;

@Data
@Builder
public class BookMovieCommand {

    @TargetAggregateIdentifier
    String movieTheatreId;

    String id;

    String userName;

    String movieName;

    MovieSlot movieSlot;

    int numberOfSeats;

    LocalDateTime date;

}
