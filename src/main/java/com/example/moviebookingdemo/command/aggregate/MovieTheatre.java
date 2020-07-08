package com.example.moviebookingdemo.command.aggregate;

import com.example.moviebookingdemo.command.aggregate.entity.Booking;
import com.example.moviebookingdemo.command.commands.*;
import com.example.moviebookingdemo.command.events.*;
import com.example.moviebookingdemo.coreapi.CommonUtils;
import com.example.moviebookingdemo.coreapi.MovieSlot;
import com.example.moviebookingdemo.coreapi.exception.InvalidOperationException;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.moviebookingdemo.coreapi.Constants.*;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@Data
@Aggregate
public class MovieTheatre {

    @AggregateIdentifier
    private String id;

    private String name;
    private int numOfSeats;
    Map<String, MovieSlot> movies;

    private String currentMovie;

    //ToDo: check feasibility
//    @AggregateMember
    private Map<String,List<Booking>> movieBookings;

    @CommandHandler
    public MovieTheatre(CreateMovieTheatreCommand command) {
        AggregateLifecycle.apply(
                MovieTheatreCreatedEvent.builder()
                        .id(command.getId())
                        .name((command.getName()))
                        .numOfSeats(command.getNumOfSeats())
                        .movies(command.getMovies())
                        .build()
        );
    }

    @EventSourcingHandler
    public void on(MovieTheatreCreatedEvent event){
        id = event.getId();

        numOfSeats = event.getNumOfSeats();
        movies = event.getMovies();
        name = event.getName();
        //changed from random to first to avoid State change exception
        currentMovie = CommonUtils.selectFirstMovie(movies);
        movieBookings = new HashMap<>();
    }

    @CommandHandler
    public void handle(UpdateMovieTheatreCommand command) {
        AggregateLifecycle.apply(
                MovieTheatreUpdatedEvent.builder()
                        .id(command.getId())
                        .name((command.getName()))
                        .numOfSeats(command.getNumOfSeats())
                        .movies(command.getMovies())
                        .build()
        );
    }

    @EventSourcingHandler
    public void on(MovieTheatreUpdatedEvent event) throws InvalidOperationException{

        if(!this.id.equals(event.getId())) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        numOfSeats = event.getNumOfSeats();
        movies = event.getMovies();
        name = event.getName();

        currentMovie = CommonUtils.selectFirstMovie(movies);
    }

    @CommandHandler
    public void handle(DeleteMovieTheatreCommand command) {
        AggregateLifecycle.apply(new MovieTheatreDeletedEvent(command.getId()));
    }

    @EventSourcingHandler
    public void on(MovieTheatreDeletedEvent event) throws InvalidOperationException{

        if(!this.id.equals(event.getId())) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        markDeleted();
    }

    //ToDo: Saga to sync on this aggregate's Id for concurrency.
    @CommandHandler
    public void handle(BookMovieCommand command){
        AggregateLifecycle.apply(
                MovieBookedEvent.builder()
                        .id(command.getId())
                        .userId(command.getUserId())
                        .movieTheatreId(command.getMovieTheatreId())
                        .movieName(command.getMovieName())
                        .movieSlot(command.getMovieSlot())
                        .numberOfSeats(command.getNumberOfSeats())
                        .build()
        );
    }


    @EventSourcingHandler
    public void on(MovieBookedEvent event) throws InvalidOperationException{

        if(!this.id.equals(event.getMovieTheatreId())) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        if(null==movies.get(event.getMovieName())) throw new InvalidOperationException(INVALID_MOVIE);

        if(!movies.get(event.getMovieName()).equals(event.getMovieSlot())) throw new InvalidOperationException(INVALID_MOVIE_SLOT);

        if(event.getNumberOfSeats()>numOfSeats) throw new InvalidOperationException(INVALID_BOOKING_CNT_MAX);

        if(event.getNumberOfSeats()>MAX_BOOKINGS_IN_TRANSACTION) throw new InvalidOperationException(INVALID_BOOKING_CNT_THRESHOLD);

        List<Booking> existingBookings = movieBookings.get(event.getMovieName());
        if(movieBookings.size()>0 && null!= existingBookings){
            int availableBookings = numOfSeats-existingBookings.size();

            if(availableBookings < event.getNumberOfSeats()) throw new InvalidOperationException(INVALID_BOOKING_CNT_NOT_ENOUGH_AVAIL);
        }

        if(null==existingBookings) existingBookings = new ArrayList<>();

        Booking booking  = Booking.builder()
                                        .id(event.getId())
                                        .userId(event.getUserId())
                                        .movieTheatreId(event.getMovieTheatreId())
                                        .movieName(event.getMovieName())
                                        .movieSlot(event.getMovieSlot())
                                        .numOfSeatsBooked(event.getNumberOfSeats())
                                        .date(LocalDateTime.now().withSecond(0).withNano(0))  //<--   To avoid aggregate illegal state exception
                                  .build();

        if(existingBookings.contains(booking)) throw new InvalidOperationException(INVALID_BOOKING_STATE_ALREADY_EXISTS);

        existingBookings.add(booking);
        movieBookings.put(event.getMovieName(), existingBookings);
    }

    //required by axon
    protected MovieTheatre() {
    }

}
