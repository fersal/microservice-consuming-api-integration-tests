package com.fersal.restmicroservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fersal.restmicroservice.model.CreditCard;
import com.fersal.restmicroservice.service.SpreedlyWebClientImpl;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertFalse;

/*
     Coded by fernando.salazar on 4/10/20
*/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreditCardControllerTest {
    @Value("${spreedly.base-url}")
    private String spreedlyBaseUrl;

    @LocalServerPort
    int localPort;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8081);

    @Test
    public void tokenizeMockingSpreedlyWithWiremock() throws JsonProcessingException {
        setupWiremockForSpreedly();
        JsonPath creditCardToken =
                RestAssured.given()
                        .baseUri("http://localhost:" + localPort)
                        .basePath("/v1/credit_card/tokenize")
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body(buildCreditCardBody())
                        .when()
                        .post()
                        .then()
                        .log()
                        .all(true)
                        .assertThat()
                        .statusCode(200)
                        .extract()
                        .jsonPath();

        assertFalse(StringUtils.isEmpty(creditCardToken.getString("token")));
    }

    private void setupWiremockForSpreedly() {
        stubFor(
                post(spreedlyBaseUrl + SpreedlyWebClientImpl.CREDIT_CARD_TOKENIZATION_URI)
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-type", ContentType.JSON.toString())
                                        .withBodyFile("SpreedlyTokenResponse.json")
                        )
        );

    }

    private String buildCreditCardBody() throws JsonProcessingException {
        CreditCard creditCard = CreditCard.builder()
                .firstName("Test")
                .lastName("Last")
                .address1("456 Main St")
                .address2("none")
                .number("5555555555554444")
                .month("12")
                .year("2032")
                .verificationValue("457")
                .zip("84097")
                .state("UT")
                .city("Salt Lake")
                .country("US")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return mapper.writeValueAsString(creditCard);
    }
}
