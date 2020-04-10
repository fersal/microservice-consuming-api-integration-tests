package com.fersal.restmicroservice.service;

import com.fersal.restmicroservice.model.CreditCard;
import com.fersal.restmicroservice.model.TokenizeTransactionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

/*
     Coded by fernando.salazar on 4/9/20
*/
@Slf4j
@Service
public class SpreedlyWebClientImpl implements SpreedlyWebClient {
    public static final String CREDIT_CARD_TOKENIZATION_URI = "/payment_methods.json";
    private static final int TIMEOUT_SECONDS = 10;

    private WebClient reactiveWebClient;

    public SpreedlyWebClientImpl(@Qualifier("spreedlyReactiveWebClientBuilder") WebClient.Builder webClientBuilder) {
        reactiveWebClient = webClientBuilder.build();
    }

    @Override
    public Optional<TokenizeTransactionResult> tokenizeCreditCard(final CreditCard creditCard) {
        Function<ClientResponse, Mono<Throwable>> reportSpreedlyError =
                spreedlyResponse -> {
                    String errorMsg = String.format("Spreedly returned error %s when tokenizing %s %s",
                            spreedlyResponse.statusCode().getReasonPhrase(),
                            creditCard.getFirstName(),
                            creditCard.getLastName());
                    log.error(errorMsg);
                    return Mono.error(new HttpClientErrorException(HttpStatus.BAD_REQUEST, errorMsg));
                };

        return reactiveWebClient
                .post()
                .uri(CREDIT_CARD_TOKENIZATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(creditCard), CreditCard.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, reportSpreedlyError::apply)
                .onStatus(HttpStatus::is5xxServerError, reportSpreedlyError::apply)
                .bodyToMono(TokenizeTransactionResult.class)
                .log()
                .map(Optional::ofNullable)
                .block(Duration.ofSeconds(TIMEOUT_SECONDS))
                ;
    }
}
