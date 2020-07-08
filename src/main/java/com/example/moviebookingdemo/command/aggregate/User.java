package com.example.moviebookingdemo.command.aggregate;

import com.example.moviebookingdemo.command.aggregate.entity.Ticket;
import com.example.moviebookingdemo.command.commands.CreateTicketCommand;
import com.example.moviebookingdemo.command.commands.CreateUserCommand;
import com.example.moviebookingdemo.command.events.MovieBookedEvent;
import com.example.moviebookingdemo.command.events.TicketCreatedEvent;
import com.example.moviebookingdemo.command.events.UserCreatedEvent;
import com.example.moviebookingdemo.coreapi.exception.InvalidOperationException;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.RoutingKey;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.moviebookingdemo.coreapi.Constants.INVALID_TICKET_STATE_ALREADY_EXISTS;
import static com.example.moviebookingdemo.coreapi.Constants.INVALID_USER;

@Data
@Aggregate
public class User {

    @AggregateIdentifier(routingKey = "userId")
    private String id;

    private String name;

    private String address;

    private boolean isAdmin;

    List<Ticket> tickets;

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

        tickets = new ArrayList<>();
    }


    @CommandHandler(routingKey = "userId")
    public void handle(CreateTicketCommand command){
        AggregateLifecycle.apply(
                TicketCreatedEvent.builder()
                        .id(command.getId())
                        .userId(command.getUserId())
                        .movieTheatreId(command.getMovieTheatreId())
                        .numberOfSeats(command.getNumberOfSeats())
                        .movieName(command.getMovieName())
                        .movieSlot(command.getMovieSlot())
                        .build()

        );
    }


//This didn't Worked!
//    @EventHandler
    @EventSourcingHandler
    public void on(TicketCreatedEvent event) throws InvalidOperationException {

        if(!id.equals(event.getUserId())) throw new InvalidOperationException(INVALID_USER);

        Ticket ticket = Ticket.builder()
                                .id(event.getId())
                                .userId(event.getUserId())
                                .movieTheatreId(event.getMovieTheatreId())
                                .movieName(event.getMovieName())
                                .movieSlot(event.getMovieSlot())
                                .numOfSeatsBooked(event.getNumberOfSeats())
                                .date(LocalDateTime.now().withSecond(0).withNano(0))
                                .build();

        if(tickets.contains(ticket)) throw new InvalidOperationException(INVALID_TICKET_STATE_ALREADY_EXISTS);

        tickets.add(ticket);
    }



    //required by axon
    protected User(){

    }

}
