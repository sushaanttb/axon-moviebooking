package com.example.moviebookingdemo.query.service;

import com.example.moviebookingdemo.command.events.MovieBookedEvent;
import com.example.moviebookingdemo.command.events.UserCreatedEvent;
import com.example.moviebookingdemo.coreapi.dto.TicketDTO;
import com.example.moviebookingdemo.coreapi.dto.UserDTO;
import com.example.moviebookingdemo.coreapi.exception.InvalidOperationException;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.moviebookingdemo.coreapi.Constants.INVALID_TICKET_STATE_ALREADY_EXISTS;
import static com.example.moviebookingdemo.coreapi.Constants.INVALID_USER;

@Service
public class UserService {

    /*
       This is our in-memory UserRepository
       Also, avoiding dumping the aggregate object as not sure on the impact on the Lifecycle
       hence saving in form of DTO
    */
    private final Map<String,UserDTO> availableUsers = new HashMap<>();

    public boolean doesUserExists(String userId){
        return availableUsers.containsKey(userId);
    }

    public boolean isAdmin(String userId){
        UserDTO userDTO = getUser(userId);

        if(userDTO!=null) return userDTO.isAdmin();

        return false;
    }

    public UserDTO getUser(String userId){
        return availableUsers.get(userId);
    }

    public void updateUser(String userId,UserDTO userDTO){
         availableUsers.put(userId,userDTO);
    }

    public List<TicketDTO> getAllTickets(String userId){
        return getUser(userId).getTickets();
    }

    @EventHandler
    public void on(UserCreatedEvent event){
        availableUsers.put(event.getId(),
                                    UserDTO.builder()
                                            .id(event.getId())
                                            .name(event.getName())
                                            .address(event.getAddress())
                                            .isAdmin(event.isAdmin())
                                            .tickets(new ArrayList<>())
                                            .build()
        );
    }

    @EventHandler
    private void on(MovieBookedEvent event) throws InvalidOperationException {

        String userId = event.getUserId();

        if(!doesUserExists(userId)) throw new InvalidOperationException(INVALID_USER);

        UserDTO userDTO = getUser(userId);

        List<TicketDTO> userTickets = userDTO.getTickets();

        if(null==userTickets) userTickets = new ArrayList<>();

        TicketDTO ticketDTO = TicketDTO.builder()
                .id(event.getId())
                .userId(event.getUserId())
                .movieTheatreId(event.getMovieTheatreId())
                .movieName(event.getMovieName())
                .movieSlot(event.getMovieSlot())
                .numOfSeatsBooked(event.getNumberOfSeats())
                .date(LocalDateTime.now().withSecond(0).withNano(0))
                .build();

        if(userTickets.contains(ticketDTO)) throw  new InvalidOperationException(INVALID_TICKET_STATE_ALREADY_EXISTS);

        userTickets.add(ticketDTO);

        userDTO.setTickets(userTickets);
        updateUser(userId,userDTO);
    }

}

