package com.fersal.restmicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
     Coded by fernando.salazar on 4/9/20
*/
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
@Setter
public class PaymentMethodToken {
    private String token;
    private String createdAt;
    private boolean test;
    private String lastFourDigits;
    private String cardType;
    private String firstName;
    private String lastName;
    private String month;
    private String year;
}
