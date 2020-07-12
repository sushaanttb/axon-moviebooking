package com.example.moviebookingdemo.command.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CreateUserCommand {

    @TargetAggregateIdentifier
    private String userName;

    private String address;

    private boolean isAdmin;
}
