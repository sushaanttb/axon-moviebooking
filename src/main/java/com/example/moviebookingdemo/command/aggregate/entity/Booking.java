package com.example.moviebookingdemo.command.aggregate.entity;

import com.example.moviebookingdemo.coreapi.MovieSlot;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.EntityId;

import java.time.LocalDateTime;

@Data
@Builder
public class Booking {

    //Todo: is transforming to entity really required?
    //@EntityId
    String id;

    String userId;

    String movieTheatreId;

    String movieName;

    MovieSlot movieSlot;

    int numOfSeatsBooked;

    LocalDateTime date;

}
