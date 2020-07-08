package com.example.moviebookingdemo.command.events;

import lombok.Data;

@Data
public class MovieTheatreDeletedEvent {
    private String id;

    public MovieTheatreDeletedEvent(String id) {
        this.id = id;
    }
}
