package com.example.moviebookingdemo.query.handlers;

import com.example.moviebookingdemo.command.aggregate.entity.Ticket;
import com.example.moviebookingdemo.coreapi.dto.TicketDTO;
import com.example.moviebookingdemo.query.queries.PurchaseHistoryQuery;
import com.example.moviebookingdemo.query.service.TicketService;
import org.axonframework.queryhandling.QueryHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserQueryHandler {

    @Autowired
    TicketService ticketService;

    @Autowired
    ModelMapper modelMapper;

    @QueryHandler
    public List<TicketDTO> getPurchases(PurchaseHistoryQuery query){
            List<Ticket> tickets =  ticketService.getAllTickets(query.getUserId());

            return tickets.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
    }

    private TicketDTO convertToDto(Ticket ticket) {
        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);
        return ticketDTO;
    }

//    https://stackoverflow.com/questions/15084670/how-to-get-the-class-of-type-variable-in-java-generics
//    private <T,K> K convertToDto(T t) {
//        ParameterizedType parameterizedType = (ParameterizedType) getClass()
//                .getGenericSuperclass();
//
//        @SuppressWarnings("unchecked")
//        Class<K> Kclazz = (Class<K>) parameterizedType.getActualTypeArguments()[0];
//
//        K k = modelMapper.map(t, Kclazz);
//
//        return k;
//    }
}
