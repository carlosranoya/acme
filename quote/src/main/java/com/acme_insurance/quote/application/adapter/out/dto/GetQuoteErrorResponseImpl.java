package com.acme_insurance.quote.application.adapter.out.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.ports.out.GetQuoteResponseInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GetQuoteErrorResponseImpl (

    Long id,
    Long insurancePolicyId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String productId,
    String offerId,
    String category,
    BigDecimal totalMonthlyPremiumAmount,
    BigDecimal totalCoverageAmount,
    Map<String, BigDecimal> coverages,
    List<String> assistances,
    Customer customer,
    @JsonProperty List<Map<String, Object>> errors

) implements GetQuoteResponseInterface {

    public static GetQuoteErrorResponseImpl asError(List<Map<String, Object>> errors) {
        return new GetQuoteErrorResponseImpl(
                null, null, null, null, null, null, null, null, null, null,
                null, null, errors);
    }

} 
