package com.example.moviebookingdemo.coreapi.dto;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TicketDTO {

    String id;

    String userName;

    String movieTheatreId;

    String movieName;

    MovieSlot movieSlot;

    int numOfSeatsBooked;

    LocalDateTime date;

}
