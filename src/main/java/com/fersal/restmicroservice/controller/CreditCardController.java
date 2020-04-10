package com.fersal.restmicroservice.controller;

import com.fersal.restmicroservice.model.CreditCard;
import com.fersal.restmicroservice.model.PaymentMethodToken;
import com.fersal.restmicroservice.service.CreditCardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
     Coded by fernando.salazar on 4/9/20
*/
@RestController
@RequestMapping("/v1/credit_card")
public class CreditCardController {
    private CreditCardService service;

    public CreditCardController(CreditCardService creditCardService) {
        service = creditCardService;
    }

    @PostMapping("/tokenize")
    public PaymentMethodToken tokenize(@RequestBody CreditCard creditCard) {
        return service.tonkenizeCard(creditCard);
    }
}
