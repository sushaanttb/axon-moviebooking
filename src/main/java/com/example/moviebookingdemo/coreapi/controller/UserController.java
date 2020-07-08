package com.example.moviebookingdemo.coreapi.controller;

import com.example.moviebookingdemo.command.commands.CreateUserCommand;
import com.example.moviebookingdemo.coreapi.dto.TicketDTO;
import com.example.moviebookingdemo.coreapi.dto.UserDTO;
import com.example.moviebookingdemo.query.queries.PurchaseHistoryQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private QueryGateway queryGateway;

    @PostMapping
    public void createUser(@RequestBody UserDTO userDTO){
        commandGateway.send(CreateUserCommand.builder()
                .id(UUID.randomUUID().toString())
                .name(userDTO.getName())
                .address(userDTO.getAddress())
                .isAdmin(userDTO.isAdmin())
                .build()
        );
    }

    @GetMapping("/purchases")
    public List<TicketDTO> getPurchases(@RequestParam(value="id") String userId) throws Exception{
        return queryGateway.query(PurchaseHistoryQuery.builder()
                                                    .userId(userId)
                                                    .build(),
                                  ResponseTypes.multipleInstancesOf(TicketDTO.class)
                                 ).join();
    }


}
