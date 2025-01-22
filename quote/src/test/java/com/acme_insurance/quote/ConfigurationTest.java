package com.acme_insurance.quote;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConfigurationTest {

    @Value("${environment.auth_service.url}")
    private String authUrl;


    @Test
    public void whenGetAuthUrl_shouldGetTheValueAtApplicationProperties() {
        assertEquals(authUrl, "http://localhost:3005/api/auth/validate");
    }

}
