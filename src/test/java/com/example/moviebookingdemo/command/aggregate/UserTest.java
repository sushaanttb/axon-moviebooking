package com.example.moviebookingdemo.command.aggregate;

import com.example.moviebookingdemo.command.commands.BookMovieCommand;
import com.example.moviebookingdemo.command.commands.CreateTicketCommand;
import com.example.moviebookingdemo.command.commands.CreateUserCommand;
import com.example.moviebookingdemo.command.events.MovieBookedEvent;
import com.example.moviebookingdemo.command.events.UserCreatedEvent;
import com.example.moviebookingdemo.coreapi.CommonUtils;
import com.example.moviebookingdemo.coreapi.MovieSlot;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private FixtureConfiguration<User> fixture;

    @BeforeEach
    public void setup(){
        fixture  = new AggregateTestFixture<>(User.class);
    }

    String aggregateId = UUID.randomUUID().toString();

    private final String DEFAULT_USER_NAME = "Default User";
    private final String DEFAULT_USER_ADDRESS = "Default User Address";

    private Map<String, MovieSlot> defaultMovies = Map.of("Movie_1",MovieSlot.MORNING,
            "Movie_2",MovieSlot.AFTERNOON,
            "Movie_3",MovieSlot.EVENING);


    CreateUserCommand createUserCommand = CreateUserCommand.builder()
            .id(aggregateId)
            .name(DEFAULT_USER_NAME)
            .address(DEFAULT_USER_ADDRESS)
            .isAdmin(false)
            .build();

    @Test
    public void testCreateUserCommand(){
        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .id(aggregateId)
                .name(DEFAULT_USER_NAME)
                .address(DEFAULT_USER_ADDRESS)
                .isAdmin(false)
                .build();

        fixture.givenNoPriorActivity()
                .when(createUserCommand)
                .expectSuccessfulHandlerExecution()
                .expectEvents(userCreatedEvent);

    }

    @Test
    public void testBookMovieCommand(){
        String movieName = CommonUtils.selectFirstMovie(defaultMovies);
        String bookingId = UUID.randomUUID().toString();
        String movieTheatreId = UUID.randomUUID().toString();
        int numOfTickets = 1;


         CreateTicketCommand createTicketCommand = CreateTicketCommand.builder()
                .id(bookingId)
                .userId(aggregateId)
                .movieTheatreId(movieTheatreId)
                .movieName(movieName)
                .movieSlot(defaultMovies.get(movieName))
                .numberOfSeats(numOfTickets)
                .build();

        fixture.givenCommands(createUserCommand)
                .when(createTicketCommand)
                .expectSuccessfulHandlerExecution()
                .expectState(state -> assertEquals(state.getTickets().size(),numOfTickets));

    }

}
