package com.example.moviebookingdemo.command.aggregate;

import com.example.moviebookingdemo.command.commands.CreateUserCommand;
import com.example.moviebookingdemo.command.events.UserCreatedEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Data
@Aggregate
public class User {

    @AggregateIdentifier
    private String userName;

    private String password;
    private String address;
    private boolean isAdmin;

    @CommandHandler
    public User(CreateUserCommand command){
        AggregateLifecycle.apply(
                UserCreatedEvent.builder()
                        .userName((command.getUserName()))
                        .password(UUID.randomUUID().toString())
                        .address(command.getAddress())
                        .isAdmin(command.isAdmin())
                    .build()

        );
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event){
        userName = event.getUserName();
        address = event.getAddress();
        password = event.getPassword();
        isAdmin = event.isAdmin();
    }

    //required by axon
    protected User(){}

}
