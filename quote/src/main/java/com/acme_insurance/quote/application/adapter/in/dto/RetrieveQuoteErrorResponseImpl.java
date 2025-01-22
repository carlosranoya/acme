package com.acme_insurance.quote.application.adapter.in.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.ports.in.RetrieveQuoteResponseInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RetrieveQuoteErrorResponseImpl (

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

) implements RetrieveQuoteResponseInterface {

    public static RetrieveQuoteErrorResponseImpl asError(List<Map<String, Object>> errors) {
        return new RetrieveQuoteErrorResponseImpl(
                null, null, null, null, null, null, null, null, null, null,
                null, null, errors);
    }

} 
