package com.acme_insurance.quote.ports.in;

public interface QuoteCreator {

    CreateQuoteResponseInterface create(CreateQuoteRequestInterface request);

}
