package com.example.moviebookingdemo.command.aggregate;

import com.example.moviebookingdemo.command.commands.*;
import com.example.moviebookingdemo.command.events.*;
import com.example.moviebookingdemo.coreapi.exception.InvalidOperationException;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import static com.example.moviebookingdemo.coreapi.Constants.*;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@Data
@Aggregate
public class MovieTheatre {

    @AggregateIdentifier
    private String id;

    private String name;

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
        name = event.getName();
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

        name = event.getName();

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
                        .userId(command.getUserId())
                        .movieTheatreId(command.getMovieTheatreId())
                        .movieName(command.getMovieName())
                        .movieSlot(command.getMovieSlot())
                        .numberOfSeats(command.getNumberOfSeats())
                        .build()
        );
    }


    @EventSourcingHandler
    public void on(BookMovieEvent event) throws InvalidOperationException{

        if(!this.id.equals(event.getMovieTheatreId())) throw new InvalidOperationException(INVALID_MOVIE_THEATRE);
    }

    //required by axon
    protected MovieTheatre() {
    }

}
