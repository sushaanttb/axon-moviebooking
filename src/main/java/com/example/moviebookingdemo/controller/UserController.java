package com.example.moviebookingdemo.controller;

import com.example.moviebookingdemo.command.commands.CreateUserCommand;
import com.example.moviebookingdemo.coreapi.dto.UserDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CommandGateway gateway;

    @PostMapping
    public void createUser(@RequestBody UserDTO userDTO){
        gateway.send(CreateUserCommand.builder()
                .id(UUID.randomUUID().toString())
                .name(userDTO.getName())
                .address(userDTO.getAddress())
                .isAdmin(userDTO.isAdmin())
                .build()
        );
    }

//    @GetMapping("/purchases")
//    public void getPurchases(@RequestParam(value="id") String userId){
//
//    }


}
