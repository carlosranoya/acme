package com.acme_insurance.quote.domain.value_object;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class ProductParsingTest {

    String productString = """
{
    "active": true,
    "created_at": "2021-07-01T00:00:00Z",
    "id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
    "name": "Seguro de Vida",
    "offers": [
        "adc56d77-348c-4bf0-908f-22d402ee715c",
        "bdc56d77-348c-4bf0-908f-22d402ee715c",
        "cdc56d77-348c-4bf0-908f-22d402ee715c",
        "a49acc20-3e2f-4784-bdb4-502de3139678"
    ]
}
    """;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenObjectMapperParseString_shouldReturnOfferObject() {

        try {
            Product product = mapper.readValue(productString, Product.class);
            assertEquals("1b2da7cc-b367-4196-8a78-9cfeec21f587", product.id());
            assertArrayEquals(product.offers().toArray(), new String[] {
                "adc56d77-348c-4bf0-908f-22d402ee715c",
                "bdc56d77-348c-4bf0-908f-22d402ee715c",
                "cdc56d77-348c-4bf0-908f-22d402ee715c",
                "a49acc20-3e2f-4784-bdb4-502de3139678"
            });
        } catch (JsonProcessingException e) {
            assertNull(e);
        }

    }

}
