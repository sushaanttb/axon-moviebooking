package com.example.moviebookingdemo.command.aggregate;

import com.example.moviebookingdemo.command.commands.CreateUserCommand;
import com.example.moviebookingdemo.command.events.UserCreatedEvent;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Data
@Aggregate
public class User {

    @AggregateIdentifier
    private String id;

    private String name;

    private String address;

    private boolean isAdmin;

    @CommandHandler
    public User(CreateUserCommand command){
        AggregateLifecycle.apply(
                UserCreatedEvent.builder()
                        .id(command.getId())
                        .name((command.getName()))
                        .address(command.getAddress())
                        .isAdmin(command.isAdmin())
                        .build()

        );
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event){
        id = event.getId();

        name = event.getName();
        address = event.getAddress();
        isAdmin = event.isAdmin();
    }


    //required by axon
    protected User(){

    }

}
