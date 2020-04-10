package com.fersal.restmicroservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fersal.restmicroservice.model.CreditCard;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreditCardControllerTest_IT {
    @LocalServerPort
    int localPort;

    @Test
    public void tokenize() throws JsonProcessingException {
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