package com.example.moviebookingdemo.command.commands;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
public class DeleteMovieTheatreCommand {

    @TargetAggregateIdentifier
    private String id;

    public DeleteMovieTheatreCommand(String id) {
        this.id = id;
    }
}
