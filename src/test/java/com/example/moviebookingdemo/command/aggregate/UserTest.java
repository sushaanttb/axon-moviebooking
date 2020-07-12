package com.example.moviebookingdemo.command.aggregate;


import com.example.moviebookingdemo.command.commands.CreateUserCommand;
import com.example.moviebookingdemo.command.events.UserCreatedEvent;
import com.example.moviebookingdemo.coreapi.MovieSlot;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;


public class UserTest {
    private FixtureConfiguration<User> fixture;

    @BeforeEach
    public void setup(){
        fixture  = new AggregateTestFixture<>(User.class);
    }


    private final String DEFAULT_USER_NAME = "defaultUserName";
    private final String DEFAULT_USER_ADDRESS = "Default User Address";

    private Map<String, MovieSlot> defaultMovies = Map.of("Movie_1",MovieSlot.MORNING,
            "Movie_2",MovieSlot.AFTERNOON,
            "Movie_3",MovieSlot.EVENING);


    CreateUserCommand createUserCommand = CreateUserCommand.builder()
            .userName(DEFAULT_USER_NAME)
            .address(DEFAULT_USER_ADDRESS)
            .isAdmin(false)
            .build();

    @Test
    public void testCreateUserCommand(){
//        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
//                .userName(DEFAULT_USER_NAME)
//                .address(DEFAULT_USER_ADDRESS)
//                .isAdmin(false)
//                .build();

        fixture.givenNoPriorActivity()
                .when(createUserCommand)
                .expectSuccessfulHandlerExecution();
//                .expectEvents(userCreatedEvent);

    }

}
