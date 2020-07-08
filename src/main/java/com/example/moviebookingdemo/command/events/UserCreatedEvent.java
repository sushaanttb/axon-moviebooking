package com.example.moviebookingdemo.command.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreatedEvent {

    private String id;

    private String name;

    private String address;

    private boolean isAdmin;
}
