package com.example.moviebookingdemo.coreapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String id;

    private String name;

    private String address;

    private boolean isAdmin;

}
