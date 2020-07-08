package com.example.moviebookingdemo.query.projections;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentlyScreenedMovieDTO {

    private String id;

    private String name;

    private String currentMovie;
}
