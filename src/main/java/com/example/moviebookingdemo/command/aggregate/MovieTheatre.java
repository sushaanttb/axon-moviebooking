package com.example.moviebookingdemo.command.aggregate;

import com.example.moviebookingdemo.command.commands.*;
import com.example.moviebookingdemo.command.events.*;
import com.example.moviebookingdemo.coreapi.MovieSlot;
import com.example.moviebookingdemo.coreapi.exception.InvalidOperationException;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Map;

import static com.example.moviebookingdemo.coreapi.Constants.*;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@Data
@Aggregate
public class MovieTheatre {

    @AggregateIdentifier
    private String id;

    private String name;

    private int capacity;

    Map<String, MovieSlot> movies;

//    ignoring due to randomization of currentMovie
//    private String currentMovie

//    ToDo: to decide to keep bookings here?
//    Map<String, List<Booking>> bookings;

    @CommandHandler
    public MovieTheatre(CreateMovieTheatreCommand command) {
        AggregateLifecycle.apply(
                MovieTheatreCreatedEvent.builder()
                        .id(command.getId())
                        .name((command.getName()))
                        .capacity(command.getCapacity())
                        .movies(command.getMovies())
                        .build()
        );
    }

    @EventSourcingHandler
    public void on(MovieTheatreCreatedEvent event){
        id = event.getId();
        name = event.getName();
        capacity = event.getCapacity();
        movies = event.getMovies();
    }

    @CommandHandler
    public void handle(UpdateMovieTheatreCommand command) {
        AggregateLifecycle.apply(
                MovieTheatreUpdatedEvent.builder()
                        .id(command.getId())
                        .name((command.getName()))
                        .capacity(command.getCapacity())
                        .movies(command.getMovies())
                        .build()
        );
    }

    @EventSourcingHandler
    public void on(MovieTheatreUpdatedEvent event) throws InvalidOperationException{

        if(!this.id.equals(event.getId())) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        name = event.getName();
        capacity = event.getCapacity();
        movies = event.getMovies();
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

    //ToDo: Saga
    @CommandHandler
    public void handle(BookMovieCommand command){
        AggregateLifecycle.apply(
                BookMovieEvent.builder()
                        .id(command.getId())
                        .userName(command.getUserName())
                        .movieTheatreId(command.getMovieTheatreId())
                        .movieName(command.getMovieName())
                        .movieSlot(command.getMovieSlot())
                        .numberOfSeats(command.getNumberOfSeats())
                        .date(command.getDate())
                        .build()
        );
    }


    @EventSourcingHandler
    public void on(BookMovieEvent event) throws InvalidOperationException{

        //Basic MovieTheatre Validations
        if(!this.id.equals(event.getMovieTheatreId())) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);

        if(null==movies.get(event.getMovieName())) throw new InvalidOperationException(INVALID_MOVIE);

        if(!movies.get(event.getMovieName()).equals(event.getMovieSlot())) throw new InvalidOperationException(INVALID_MOVIE_SLOT);

        if(event.getNumberOfSeats()> capacity) throw new InvalidOperationException(INVALID_BOOKING_CNT_MAX);

        if(event.getNumberOfSeats()>MAX_BOOKINGS_IN_TRANSACTION) throw new InvalidOperationException(INVALID_BOOKING_CNT_THRESHOLD);

    }

    //required by axon
    protected MovieTheatre() {}

}
