package com.acme_insurance.quote.domain.value_object;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class OfferParsingTest {

    String offerString = """
{
	"active": true,
	"assistances": [
		"Eletricista",
		"Chaveiro 24h",
		"Assistência Funerária"
	],
	"coverages": {
		"Desastres naturais": 600000.0,
		"Incêndio": 500000.0,
		"Responsabiliadade civil": 80000.0,
		"Roubo": 100000.0
	},
	"created_at": "2021-07-01T00:00:00Z",
	"id": "adc56d77-348c-4bf0-908f-22d402ee715c",
	"monthly_premium_amount": {
		"max_amount": 100.74,
		"min_amount": 50.0,
		"suggested_amount": 60.25
	},
	"name": "Seguro de Vida Familiar",
	"product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587"
}  
    """;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenObjectMapperParseString_shouldReturnOfferObject() {

        try {
            Offer offer = mapper.readValue(offerString, Offer.class);
            assertEquals("adc56d77-348c-4bf0-908f-22d402ee715c", offer.id());
        } catch (JsonProcessingException e) {
            assertNull(e);
        }

    }

}
