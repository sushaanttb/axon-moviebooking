package com.example.moviebookingdemo.query.projections;

import lombok.Data;

@Data
public class CurrentlyScreenedMovieDTO {

    private String id;

    private String name;

    private String currentMovie;
}
