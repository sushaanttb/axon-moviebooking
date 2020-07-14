package com.example.moviebookingdemo.query.service;

import com.example.moviebookingdemo.command.aggregate.entity.Ticket;
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
import java.util.stream.Collectors;

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

    public boolean doesUserExists(String userName){
        return availableUsers.containsKey(userName);
    }

    public boolean isAdmin(String userName){
        UserDTO userDTO = getUser(userName);

        if(userDTO!=null) return userDTO.isAdmin();

        return false;
    }

    public UserDTO getUser(String userName){
        return availableUsers.get(userName);
    }

    public void updateUser(String userName,UserDTO userDTO){
         availableUsers.put(userName,userDTO);
    }

    public List<TicketDTO> getAllTickets(String userId){
        List<TicketDTO> tickets = getUser(userId).getTickets().stream()
                //ToDo: backward compatibility check when date was not part of it
                //remove it post clearing all aggregates
                .filter(ticketDTO -> ticketDTO.getDate()!=null)
                .collect(Collectors.toList());

        tickets.sort((ticket1,ticket2)->{
            if(ticket1.getDate().isBefore(ticket2.getDate())) return 1;
            else if (ticket1.getDate().isAfter(ticket2.getDate())) return -1;
            else return 0;
        });
        return tickets;
    }

    public UserDTO login(String userName, String password ){
        UserDTO existingUser = availableUsers.get(userName);

        return ((null==existingUser) || (!password.equals(existingUser.getPassword())))
                ?null : existingUser;
    }

    @EventHandler
    public void on(UserCreatedEvent event){
        availableUsers.put(event.getUserName(),
                                    UserDTO.builder()
                                        .userName(event.getUserName())
                                        .address(event.getAddress())
                                        .password(event.getPassword())
                                        .isAdmin(event.isAdmin())
                                        .tickets(new ArrayList<>())
                                .build()
        );
    }

    @EventHandler
    private void on(MovieBookedEvent event) throws InvalidOperationException {

        String userName = event.getUserName();

        if(!doesUserExists(userName)) throw new InvalidOperationException(INVALID_USER);

        UserDTO userDTO = getUser(userName);

        List<TicketDTO> userTickets = userDTO.getTickets();

        if(null==userTickets) userTickets = new ArrayList<>();

        TicketDTO ticketDTO = TicketDTO.builder()
                .id(event.getId())
                .userName(event.getUserName())
                .movieTheatreId(event.getMovieTheatreId())
                .movieName(event.getMovieName())
                .movieSlot(event.getMovieSlot())
                .numOfSeatsBooked(event.getNumberOfSeats())
                .date(event.getDate())
                .build();

        if(userTickets.contains(ticketDTO)) throw  new InvalidOperationException(INVALID_TICKET_STATE_ALREADY_EXISTS);

        userTickets.add(ticketDTO);

        userDTO.setTickets(userTickets);
        updateUser(userName,userDTO);
    }

}

