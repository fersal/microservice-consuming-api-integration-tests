package com.fersal.restmicroservice.model;

import lombok.*;

/*
     Coded by fernando.salazar on 4/9/20
*/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    private String firstName;
    private String lastName;
    private String number;
    private String verificationValue;
    private String month;
    private String year;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zip;
}
