package com.example.moviebookingdemo.command.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CreateUserCommand {

    @TargetAggregateIdentifier
    private String id;

    private String name;

    private String address;

    private boolean isAdmin;
}
