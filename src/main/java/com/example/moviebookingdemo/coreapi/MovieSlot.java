package com.example.moviebookingdemo.coreapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MovieSlot {
    MORNING("Morning"),AFTERNOON("Afternoon"),EVENING("Evening");

    private final String value;

    MovieSlot(String val) {
        this.value=val;
    }

    @JsonValue
    public String getValue(){
        return this.value;
    }
}
