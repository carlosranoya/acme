package com.acme_insurance.quote.application.adapter.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.acme_insurance.quote.application.adapter.out.dto.GetQuoteErrorResponseImpl;
import com.acme_insurance.quote.application.repository.QuoteRepository;
import com.acme_insurance.quote.ports.dto.Response;
import com.acme_insurance.quote.ports.out.GetQuoteRequestInterface;
import com.acme_insurance.quote.ports.out.GetQuoteResponseInterface;
import com.acme_insurance.quote.ports.out.QuoteGetterInterface;

@Repository
public class QuoteGetterImpl implements QuoteGetterInterface {

    @Autowired
    private QuoteRepository quoteRepository;

    @Override
    public GetQuoteResponseInterface get(GetQuoteRequestInterface request) {

        try {
            return quoteRepository.findByCustomId(request.id());
            
        } catch (Exception e) {
            return GetQuoteErrorResponseImpl.asError(Response.getErrorList(e));
        }

    }

}
