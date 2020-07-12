package com.example.moviebookingdemo.command.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreatedEvent {

    private String userName;

    private String address;

    private boolean isAdmin;

    private String password;
}
