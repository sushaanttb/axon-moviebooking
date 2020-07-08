package com.example.moviebookingdemo.query.service;

import com.example.moviebookingdemo.command.aggregate.entity.Ticket;
import com.example.moviebookingdemo.command.events.MovieBookedEvent;
import com.example.moviebookingdemo.coreapi.exception.InvalidOperationException;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.moviebookingdemo.coreapi.Constants.INVALID_TICKET_STATE_ALREADY_EXISTS;

@Service
public class TicketService {

    private final Map<String, List<Ticket>> userTickets = new HashMap<>();

    public List<Ticket> getAllTickets(String userId){
        return userTickets.get(userId);
    }

    @EventHandler
    private void on(MovieBookedEvent event) throws InvalidOperationException {

        String userId = event.getUserId();

        List<Ticket> tickets = userTickets.get(userId);
        if(null==tickets) tickets = new ArrayList<>();

        Ticket ticket = Ticket.builder()
                .id(event.getId())
                .userId(event.getUserId())
                .movieTheatreId(event.getMovieTheatreId())
                .movieName(event.getMovieName())
                .movieSlot(event.getMovieSlot())
                .numOfSeatsBooked(event.getNumberOfSeats())
                .date(LocalDateTime.now().withSecond(0).withNano(0))
                .build();

        if(tickets.contains(ticket)) throw  new InvalidOperationException(INVALID_TICKET_STATE_ALREADY_EXISTS);

        tickets.add(ticket);
        userTickets.put(userId,tickets);
    }
}
