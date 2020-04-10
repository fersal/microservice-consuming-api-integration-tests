package com.fersal.restmicroservice.service;

import com.fersal.restmicroservice.model.CreditCard;
import com.fersal.restmicroservice.model.PaymentMethodToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Supplier;

/*
     Coded by fernando.salazar on 4/9/20
*/
@Service
public class CreditCardService {
    private SpreedlyWebClient spreedlyWebClient;

    public CreditCardService(SpreedlyWebClient webClient) {
        spreedlyWebClient = webClient;
    }

    public PaymentMethodToken tonkenizeCard(final CreditCard creditCard) {
        Supplier<IllegalStateException> illegalStateExceptionSupplier =
                () -> new IllegalStateException("Error while tokenizing card! Make card has first and last names and ZIP");

        return verifyCard(creditCard)
                .flatMap(spreedlyWebClient::tokenizeCreditCard)
                .map(result -> result.getTransaction().getPaymentMethod())
                .orElseThrow(illegalStateExceptionSupplier);
    }

    Optional<CreditCard> verifyCard(final CreditCard creditCard) {
        return Optional.ofNullable(creditCard)
                .map(card -> StringUtils.isEmpty(card.getFirstName()) ? null : card)
                .map(card -> StringUtils.isEmpty(card.getLastName()) ? null : card)
                .map(card -> StringUtils.isEmpty(card.getZip()) ? null : card);
    }
}
