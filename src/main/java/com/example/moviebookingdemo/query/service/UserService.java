package com.example.moviebookingdemo.query.service;

import com.example.moviebookingdemo.command.events.UserCreatedEvent;
import com.example.moviebookingdemo.coreapi.dto.UserDTO;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<String,UserDTO> availableUsers = new HashMap<>();

    public boolean doesUserExists(String userId){
        if(availableUsers.get(userId)!=null) return true;

        return false;
    }

    public boolean isAdmin(String userId){
        UserDTO userDTO = availableUsers.get(userId);

        if(userDTO!=null) return userDTO.isAdmin();

        return false;
    }

    @EventHandler
    public void on(UserCreatedEvent event){
        availableUsers.put(event.getId(),
                UserDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .address(event.getAddress())
                .isAdmin(event.isAdmin())
                .build()
        );
    }

}

