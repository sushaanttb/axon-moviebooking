package com.example.moviebookingdemo.coreapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {

    private String id;

    private String name;

    private String address;

    private boolean isAdmin;

    private List<TicketDTO> tickets;

}
