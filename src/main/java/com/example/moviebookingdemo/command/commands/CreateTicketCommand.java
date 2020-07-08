package com.example.moviebookingdemo.command.commands;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CreateTicketCommand {

    @TargetAggregateIdentifier
    String userId;

    String id;

    String movieTheatreId;

    String movieName;

    MovieSlot movieSlot;

    int numberOfSeats;
}
