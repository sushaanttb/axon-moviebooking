package com.example.moviebookingdemo.query.handlers;

import com.example.moviebookingdemo.coreapi.dto.MovieTheatreDTO;
import com.example.moviebookingdemo.query.projections.CurrentlyScreenedMovieDTO;
import com.example.moviebookingdemo.query.queries.AllMovieTheatresQuery;
import com.example.moviebookingdemo.query.queries.AvailableMovieSlotsQuery;
import com.example.moviebookingdemo.query.queries.CurrentlyScreenedMoviesQuery;
import com.example.moviebookingdemo.query.service.MovieTheatreService;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieTheatreQueryHandler {

    @Autowired
    private MovieTheatreService movieTheatreService;

    @QueryHandler
    public List<MovieTheatreDTO> getAllMovieTheatres(AllMovieTheatresQuery allMovieTheatresQuery){
        return movieTheatreService.getAllMovieTheatres();
    }

    @QueryHandler
    public List<MovieTheatreDTO> getAvailableMovieSlots(AvailableMovieSlotsQuery availableMovieSlotsQuery){
        return movieTheatreService.getAllAvailableMovieSlots();
    }

    @QueryHandler
    public List<CurrentlyScreenedMovieDTO> getCurrentlyScreenedMovies(CurrentlyScreenedMoviesQuery currentlyScreenedMoviesQuery){
        return movieTheatreService.getAllCurrentlyScreenedMovies();
    }
}
