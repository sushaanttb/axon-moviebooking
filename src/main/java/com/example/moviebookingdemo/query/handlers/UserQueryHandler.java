package com.example.moviebookingdemo.query.handlers;

import com.example.moviebookingdemo.coreapi.dto.TicketDTO;
import com.example.moviebookingdemo.query.queries.PurchaseHistoryQuery;
import com.example.moviebookingdemo.query.service.UserService;
import org.axonframework.queryhandling.QueryHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserQueryHandler {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @QueryHandler
    public List<TicketDTO> getPurchases(PurchaseHistoryQuery query){
          return userService.getAllTickets(query.getUserId());

    }

}
