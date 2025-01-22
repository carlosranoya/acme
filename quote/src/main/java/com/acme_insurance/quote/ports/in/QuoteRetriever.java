package com.acme_insurance.quote.ports.in;

public interface QuoteRetriever {

    RetrieveQuoteResponseInterface get(RetrieveQuoteRequestInterface request);

}
