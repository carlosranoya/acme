package com.acme_insurance.quote.ports.out;

public interface QuoteSaverInterface {

    SaveQuoteResponseInterface save(SaveQuoteRequestInterface request);

}
