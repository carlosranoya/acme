package com.acme_insurance.quote.application.adapter.in.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.ports.in.CreateQuoteRequestInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateQuoteRequestImpl (

    @JsonProperty("product_id") String productId,
    @JsonProperty("offer_id") String offerId,
    @JsonProperty String category,
    @JsonProperty("total_monthly_premium_amount") BigDecimal totalMonthlyPremiumAmount,
    @JsonProperty("total_coverage_amount") BigDecimal totalCoverageAmount,
    @JsonProperty Map<String, BigDecimal> coverages,
    @JsonProperty List<String> assistances,
    @JsonProperty Customer customer

) implements CreateQuoteRequestInterface
{

}

// {
//     "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
//     "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
//     "category": "HOME",
//     "total_monthly_premium_amount": 75.25,
//     "total_coverage_amount": 825000.00,
//     "coverages": {
//     "Incêndio": 250000.00,
//     "Desastres naturais": 500000.00,
//     "Responsabiliadade civil": 75000.00
//     },
//     "assistances": [
//     "Encanador",
//     "Eletricista",
//     "Chaveiro 24h"
//     ],
//     "customer": {
//     "document_number": "36205578900",
//     "name": "John Wick",
//     "type": "NATURAL",
//     "gender": "MALE",
//     "date_of_birth": "1973-05-02",
//     "email": "johnwick@gmail.com",
//     "phone_number": 11950503030
//     }
//     }