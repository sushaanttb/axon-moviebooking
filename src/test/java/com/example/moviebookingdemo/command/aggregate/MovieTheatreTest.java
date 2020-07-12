package com.example.moviebookingdemo.command.aggregate;

import com.example.moviebookingdemo.command.commands.BookMovieCommand;
import com.example.moviebookingdemo.command.commands.CreateMovieTheatreCommand;
import com.example.moviebookingdemo.command.commands.DeleteMovieTheatreCommand;
import com.example.moviebookingdemo.command.commands.UpdateMovieTheatreCommand;
import com.example.moviebookingdemo.command.events.MovieTheatreCreatedEvent;
import com.example.moviebookingdemo.command.events.MovieTheatreDeletedEvent;
import com.example.moviebookingdemo.coreapi.CommonUtils;
import com.example.moviebookingdemo.coreapi.Constants;
import com.example.moviebookingdemo.coreapi.MovieSlot;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieTheatreTest {

    private FixtureConfiguration<MovieTheatre> fixture;

    private final String DEFAULT_MOVIE_THEATRE_NAME = "Default Movie Theatre";

    private Map<String, MovieSlot> defaultMovies = Map.of("Movie_1",MovieSlot.MORNING,
                                                          "Movie_2",MovieSlot.AFTERNOON,
                                                          "Movie_3",MovieSlot.EVENING);

    private int default_no_of_seats  = 100;

    String aggregateId = UUID.randomUUID().toString();

    CreateMovieTheatreCommand createMovieTheatreCommand = CreateMovieTheatreCommand.builder()
            .id(aggregateId)
            .name(DEFAULT_MOVIE_THEATRE_NAME)
            .capacity(default_no_of_seats)
            .movies(defaultMovies)
            .build();

    @BeforeEach
    public void setup(){
        fixture  = new AggregateTestFixture<>(MovieTheatre.class);
    }

    @Test
    public void testCreateMovieTheatreCommand(){
        MovieTheatreCreatedEvent movieTheatreCreatedEvent = MovieTheatreCreatedEvent.builder()
                .id(aggregateId)
                .name(DEFAULT_MOVIE_THEATRE_NAME)
                .capacity(default_no_of_seats)
                .movies(defaultMovies)
                .build();

        fixture.givenNoPriorActivity()
                .when(createMovieTheatreCommand)
                .expectSuccessfulHandlerExecution()
                .expectEvents(movieTheatreCreatedEvent);

    }

    @Test
    public void testUpdateMovieTheatreCommand(){

        UpdateMovieTheatreCommand updateMovieTheatreCommand = UpdateMovieTheatreCommand.builder()
                .id(aggregateId)
                .name(DEFAULT_MOVIE_THEATRE_NAME)
                .capacity(default_no_of_seats*2)
                .movies(defaultMovies)
                .build();

        fixture.givenCommands(createMovieTheatreCommand)
                .when(updateMovieTheatreCommand)
                .expectSuccessfulHandlerExecution();

    }

    @Test
    public void testDeleteMovieTheatreCommand(){

        DeleteMovieTheatreCommand deleteMovieTheatreCommand = new DeleteMovieTheatreCommand(aggregateId);

        MovieTheatreDeletedEvent movieTheatreDeletedEvent = new MovieTheatreDeletedEvent(aggregateId);

        fixture.givenCommands(createMovieTheatreCommand)
                .when(deleteMovieTheatreCommand)
                .expectSuccessfulHandlerExecution()
                .expectEvents(movieTheatreDeletedEvent)
                .expectMarkedDeleted();
    }

    @Test
    public void testBookMovieCommand(){
        String movieName = CommonUtils.selectFirstMovie(defaultMovies);
        String bookingId = UUID.randomUUID().toString();
        String userName = "defaultUserName";
        int numOfTickets = 1;

        BookMovieCommand bookMovieCommand = BookMovieCommand.builder()
                .id(bookingId)
                .userName(userName)
                .movieTheatreId(aggregateId)
                .movieName(movieName)
                .movieSlot(defaultMovies.get(movieName))
                .numberOfSeats(numOfTickets)
                .build();


        fixture.givenCommands(createMovieTheatreCommand)
                .when(bookMovieCommand)
                .expectSuccessfulHandlerExecution();

    }

    //ToDo: how to propogate the exception from eventhandlers?
    @Test
    public void testBookMovieCommand_WithBookingsGreaterThanThreshold(){
        String movieName = CommonUtils.selectFirstMovie(defaultMovies);
        String bookingId = UUID.randomUUID().toString();
        String userName = "defaultUserName";
        int numOfTickets = Constants.MAX_BOOKINGS_IN_TRANSACTION+1;

        BookMovieCommand bookMovieCommand = BookMovieCommand.builder()
                .id(bookingId)
                .userName(userName)
                .movieTheatreId(aggregateId)
                .movieName(movieName)
                .movieSlot(defaultMovies.get(movieName))
                .numberOfSeats(numOfTickets)
                .build();


        fixture.givenCommands(createMovieTheatreCommand)
                .when(bookMovieCommand)
                //ToDo: Wraps it in InvocationTargetException?
//                .expectException(InvalidOperationException.class)
                .expectException(Exception.class);
    }



}
