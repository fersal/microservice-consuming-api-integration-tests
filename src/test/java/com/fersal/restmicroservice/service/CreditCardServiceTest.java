package com.fersal.restmicroservice.service;

import com.fersal.restmicroservice.model.CreditCard;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;

public class CreditCardServiceTest {
    private static final String LAST_NAME = "lName";
    private static final String FIRST_NAME = "fName";

    @InjectMocks
    private CreditCardService sut;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verifyCardWithRequiredFieldsSucceeds() {
        CreditCard card = CreditCard.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .number("378282246310005")
                .verificationValue("3456")
                .year("2055")
                .month("12")
                .zip("12345").build();
        Optional<CreditCard> result = sut.verifyCard(card);

        assertFalse(result.isEmpty());
        assertEquals(LAST_NAME, result.orElseThrow().getLastName());
        assertEquals("12345", result.orElseThrow().getZip());
    }

    @Test
    public void verifyCardWithoutZipFails() {
        CreditCard card = CreditCard.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build();
        Optional<CreditCard> result = sut.verifyCard(card);

        assertTrue(result.isEmpty());
    }
}