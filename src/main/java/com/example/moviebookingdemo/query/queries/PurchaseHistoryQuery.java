package com.example.moviebookingdemo.query.queries;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseHistoryQuery {

    private String userId;
}
