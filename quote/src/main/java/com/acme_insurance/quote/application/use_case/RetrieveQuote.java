package com.acme_insurance.quote.application.use_case;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme_insurance.quote.application.adapter.in.dto.RetrieveQuoteErrorResponseImpl;
import com.acme_insurance.quote.application.dto.QuoteData;
import com.acme_insurance.quote.ports.in.QuoteRetriever;
import com.acme_insurance.quote.ports.in.RetrieveQuoteRequestInterface;
import com.acme_insurance.quote.ports.in.RetrieveQuoteResponseInterface;
import com.acme_insurance.quote.ports.out.GetQuoteRequestInterface;
import com.acme_insurance.quote.ports.out.GetQuoteResponseInterface;
import com.acme_insurance.quote.ports.out.QuoteGetterInterface;

@Service
public class RetrieveQuote implements QuoteRetriever {

    @Autowired
    private QuoteGetterInterface quoteGetter;

    @Override
    public RetrieveQuoteResponseInterface get(RetrieveQuoteRequestInterface request) {

        GetQuoteRequestInterface getRequest = new GetQuoteRequestInterface() {
            @Override
            public Long id() {
                return request.id();
            }
        };

        GetQuoteResponseInterface response = quoteGetter.get(getRequest);

        if (response.errors() != null) {
            return RetrieveQuoteErrorResponseImpl.asError(response.errors());
        }

        return (QuoteData) response;
        
    }

}
