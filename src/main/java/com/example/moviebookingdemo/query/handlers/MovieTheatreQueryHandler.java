package com.example.moviebookingdemo.query.handlers;

import com.example.moviebookingdemo.coreapi.dto.BookingDTO;
import com.example.moviebookingdemo.coreapi.dto.MovieTheatreDTO;
import com.example.moviebookingdemo.query.projections.CurrentlyScreenedMovieDTO;
import com.example.moviebookingdemo.query.queries.*;
import com.example.moviebookingdemo.query.service.MovieTheatreService;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieTheatreQueryHandler {

    @Autowired
    private MovieTheatreService movieTheatreService;

    @QueryHandler
    public List<MovieTheatreDTO> getAllMovieTheatres(AllMovieTheatresQuery allMovieTheatresQuery){
        return movieTheatreService.getAllMovieTheatres();
    }

    @QueryHandler
    public List<MovieTheatreDTO> getAllEmptyMovieTheatres(AllEmptyMovieTheatreMoviesQuery allEmptyMovieTheatreMoviesQuery){
        return movieTheatreService.getAllEmptyMovieTheatres();
    }


    @QueryHandler
    public List<MovieTheatreDTO> getAvailableMovieSlots(AvailableMovieSlotsQuery availableMovieSlotsQuery){
        return movieTheatreService.getAllAvailableMovieSlots();
    }

    @QueryHandler
    public List<CurrentlyScreenedMovieDTO> getCurrentlyScreenedMovies(CurrentlyScreenedMoviesQuery currentlyScreenedMoviesQuery){
        return movieTheatreService.getAllCurrentlyScreenedMovies();
    }

    @QueryHandler
    public BookingDTO getBooking(GetBookingQuery getBookingQuery){
        Optional<BookingDTO> bookingDTOOptional = movieTheatreService.getBooking(
                getBookingQuery.getMovieTheatreId(),
                getBookingQuery.getMovieName(),
                getBookingQuery.getBookingId());

        if(bookingDTOOptional.isPresent()) return bookingDTOOptional.get();
        return null;
    }

}
