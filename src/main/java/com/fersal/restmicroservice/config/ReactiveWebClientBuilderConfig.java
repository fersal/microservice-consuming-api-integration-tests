package com.fersal.restmicroservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/*
     Coded by fernando.salazar on 4/7/20
*/

@Configuration
public class ReactiveWebClientBuilderConfig {
    @Value("${spreedly.base-url}")
    private String spreedlyBaseUrl;
    @Value("${spreedly.environment-key}")
    private String environmentKey;
    @Value("${spreedly.access-secret}")
    private String secret;

    @Bean("spreedlyReactiveWebClientBuilder")
    public WebClient.Builder getBuilder() {
        return WebClient.builder()
                .baseUrl(spreedlyBaseUrl)
                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(environmentKey, secret))
                .exchangeStrategies(buildSnakeCaseNamingStrategy());
    }

    /**
     * Ensure Snake-case naming is applied to JSON objects sent/received.
     *
     * @return ExchangeStrategies
     */
    private ExchangeStrategies buildSnakeCaseNamingStrategy() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
                    clientCodecConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
                })
                .build();
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder().propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
}
