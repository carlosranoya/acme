package com.acme_insurance.quote.domain.value_object;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Customer (

    @JsonProperty("document_number") String documentNumber,
    @JsonProperty String name,
    @JsonProperty String type,
    @JsonProperty String gender,
    @JsonProperty("date_of_birth") Date dateOfBirth,
    @JsonProperty String email,
    @JsonProperty("phone_number") String phoneNumber

)
{}
