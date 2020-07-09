package com.example.moviebookingdemo.coreapi.dto;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDTO {
    String id;

    String userId;

    String movieTheatreId;

    String movieName;

    MovieSlot movieSlot;

    int numOfSeatsBooked;

    LocalDateTime date;
}
