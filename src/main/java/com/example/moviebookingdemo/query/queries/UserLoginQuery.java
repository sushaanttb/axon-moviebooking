package com.example.moviebookingdemo.query.queries;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLoginQuery {

    String userName;
    String password;
}
