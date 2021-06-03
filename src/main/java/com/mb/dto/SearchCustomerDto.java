package com.mb.dto;

import lombok.Data;

@Data
public class SearchCustomerDto {
    private String firstName;
    private String lastName;
    private String username;
    private Long bonusPoints;
}
