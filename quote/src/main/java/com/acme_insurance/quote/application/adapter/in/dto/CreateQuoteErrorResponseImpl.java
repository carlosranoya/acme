package com.acme_insurance.quote.application.adapter.in.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.ports.in.CreateQuoteResponseInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateQuoteErrorResponseImpl (
    Long id,
    String productId,
    String offerId,
    String category,
    BigDecimal totalMonthlyPremiumAmount,
    BigDecimal totalCoverageAmount,
    Map<String, BigDecimal> coverages,
    List<String> assistances,
    Customer customer,
    @JsonProperty List<Map<String, Object>> errors

) implements CreateQuoteResponseInterface {

    public static CreateQuoteErrorResponseImpl asError(List<Map<String, Object>> errors) {
        return new CreateQuoteErrorResponseImpl(null, null, null, null, null,
        null, null, null, null, errors);
    }
}
