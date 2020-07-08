package com.example.moviebookingdemo.command.aggregate.entity;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Ticket {

    String id;

    String userId;

    String movieTheatreId;

    String movieName;

    MovieSlot movieSlot;

    int numOfSeatsBooked;

    LocalDateTime date;

}
