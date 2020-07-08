package com.example.moviebookingdemo.coreapi.dto;

import com.example.moviebookingdemo.command.aggregate.entity.Ticket;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private String id;

    private String name;

    private String address;

    private boolean isAdmin;

    List<Ticket> tickets;
}
