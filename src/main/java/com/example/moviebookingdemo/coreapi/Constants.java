package com.example.moviebookingdemo.coreapi;

public class Constants {

    public static int MAX_BOOKINGS_IN_TRANSACTION = 9;

    public static final String EMPTY_MOVIE_THEATRE = "Movie Theatre is empty!";
    public static final String INVALID_MOVIE_THEATRE = "Movie Theatre Doesn't exists!";
    public static final String INVALID_MOVIE = "Movie Doesn't Exists!";
    public static final String INVALID_MOVIE_SLOT = "Invalid Movie Slot!";


    public static final String INVALID_BOOKING_CNT_MAX = "No. of Bookings cannot be greater than availabile capacity!";
    public static final String INVALID_BOOKING_CNT_THRESHOLD = "No. of Bookings cannot be greater than " + MAX_BOOKINGS_IN_TRANSACTION ;
    public static final String INVALID_BOOKING_CNT_NOT_ENOUGH_AVAIL = "Not Enough Bookings available!";
    public static final String INVALID_BOOKING_STATE_ALREADY_EXISTS = "Booking Already Exists!";

    public static final String INVALID_USER = "User Doesn't exists!";
    public static final String INVALID_TICKET_STATE_ALREADY_EXISTS = "Ticket Already Exists!";


}
