package com.acme_insurance.quote.domain.value_object;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.acme_insurance.quote.utils.AssistanceDeserializer;
import com.acme_insurance.quote.utils.AssistanceSerializer;
import com.acme_insurance.quote.utils.LocalDateTimeDeserializer;
import com.acme_insurance.quote.utils.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record Offer(

    @JsonProperty String id,
    @JsonProperty("product_id") String productId,
    @JsonProperty String name,

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("created_at") LocalDateTime createdAt,

    @JsonProperty boolean active,
    @JsonProperty Map<String, BigDecimal> coverages,

    @JsonSerialize(using = AssistanceSerializer.class)
    @JsonDeserialize(using = AssistanceDeserializer.class)
    @JsonProperty List<Assistance> assistances,
    
    @JsonProperty("monthly_premium_amount") MonthlyPremiumAmount monthlyPremiumAmount
) {}



