package com.acme_insurance.quote.domain.value_object;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MonthlyPremiumAmount(
    
    @JsonProperty("max_amount") BigDecimal maxAmount,
    @JsonProperty("min_amount") BigDecimal minAmount,
    @JsonProperty("suggested_amount") BigDecimal suggestedAmount

) {}
