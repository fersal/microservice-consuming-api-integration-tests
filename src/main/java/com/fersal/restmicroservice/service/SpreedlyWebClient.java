package com.fersal.restmicroservice.service;

import com.fersal.restmicroservice.model.CreditCard;
import com.fersal.restmicroservice.model.TokenizeTransactionResult;

import java.util.Optional;

/*
     Coded by fernando.salazar on 4/9/20
*/
public interface SpreedlyWebClient {
    Optional<TokenizeTransactionResult> tokenizeCreditCard(CreditCard creditCard);
}
