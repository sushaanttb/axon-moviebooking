package com.example.moviebookingdemo.coreapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {

    private String userName;

    private String address;

    private boolean isAdmin;

    private String password;

    private List<TicketDTO> tickets;

}
