package com.acme_insurance.quote.domain.value_object;

import java.time.LocalDateTime;
import java.util.List;

import com.acme_insurance.quote.utils.LocalDateTimeDeserializer;
import com.acme_insurance.quote.utils.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record Product( 
    @JsonProperty String id,
    @JsonProperty String name,

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("created_at") LocalDateTime createdAt,
    
    @JsonProperty boolean active,
    @JsonProperty List<String> offers
)
{}
