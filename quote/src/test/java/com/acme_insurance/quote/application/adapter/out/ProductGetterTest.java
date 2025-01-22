package com.acme_insurance.quote.application.adapter.out;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.acme_insurance.quote.TestData;
import com.acme_insurance.quote.domain.value_object.Offer;
import com.acme_insurance.quote.domain.value_object.Product;
import com.acme_insurance.quote.ports.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
public class ProductGetterTest {

    static private Offer offer = TestData.activeOffer1;

    static private Product product = TestData.activeProd1;
    static private String productString = null;
    static private String offerString = null;
    static private JsonProcessingException exception;
    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        
        try {
            productString = mapper.writeValueAsString(product);
            offerString = mapper.writeValueAsString(offer);
        } catch (JsonProcessingException e) {
            exception = e;
        }     
    }

    @Mock
    private HttpClientInterface client;

    @InjectMocks
    private ProductGetterImpl prodGetter;

    @Test
    public void test() {

        assertNotNull(productString);
        assertNotNull(offerString);

        when(client.getStatus()).thenReturn(200);
        when(client.getBody()).thenReturn(productString);

        Response<Product> response = prodGetter.getProduct(product.id());

        assertNotNull(response.getData());
        assertNull(response.errors());

        assertEquals(product.id(), response.getData().id());

    }


}
