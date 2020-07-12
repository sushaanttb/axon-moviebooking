package com.example.moviebookingdemo.coreapi.controller;

import com.example.moviebookingdemo.command.commands.CreateUserCommand;
import com.example.moviebookingdemo.coreapi.dto.TicketDTO;
import com.example.moviebookingdemo.coreapi.dto.UserDTO;
import com.example.moviebookingdemo.query.queries.GetUserQuery;
import com.example.moviebookingdemo.query.queries.PurchaseHistoryQuery;
import com.example.moviebookingdemo.query.queries.UserLoginQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private QueryGateway queryGateway;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO){
        String aggregateId = commandGateway.sendAndWait(CreateUserCommand.builder()
                .userName(userDTO.getUserName())
                .address(userDTO.getAddress())
                .isAdmin(userDTO.isAdmin())
                .build()
        );

        if(null!=aggregateId)
            return queryGateway.query(GetUserQuery.builder().userName(userDTO.getUserName()).build(),
                    ResponseTypes.instanceOf(UserDTO.class)
            ).join();

        return null;
    }

    @GetMapping("/purchases")
    public List<TicketDTO> getPurchases(@RequestParam(value="username") String userName) throws Exception{
        return queryGateway.query(PurchaseHistoryQuery.builder()
                                                    .userId(userName)
                                                    .build(),
                                  ResponseTypes.multipleInstancesOf(TicketDTO.class)
                                 ).join();
    }

    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody UserDTO userDTO){
        return queryGateway.query(UserLoginQuery.builder()
                .userName(userDTO.getUserName())
                .password(userDTO.getPassword())
                .build()
                ,ResponseTypes.instanceOf(UserDTO.class)
        ).join();
    }


}
