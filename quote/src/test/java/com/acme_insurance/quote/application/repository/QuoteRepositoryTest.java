package com.acme_insurance.quote.application.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class QuoteRepositoryTest {

    @Autowired
    QuoteRepository repository;

    @Test
    void whenCreateQuote_shouldGetSameQuote() {
        // TODO:
    }

}
