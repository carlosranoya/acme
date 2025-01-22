package com.acme_insurance.quote.application.adapter.in.dto;

import com.acme_insurance.quote.ports.in.RetrieveQuoteRequestInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RetrieveQuoteRequestImpl (
    @JsonProperty Long id
) implements RetrieveQuoteRequestInterface{

}
