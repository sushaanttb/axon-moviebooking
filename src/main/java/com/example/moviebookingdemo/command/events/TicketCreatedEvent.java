package com.example.moviebookingdemo.command.events;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketCreatedEvent {

    String id;

    String userId;

    String movieTheatreId;

    String movieName;

    MovieSlot movieSlot;

    int numberOfSeats;
}
