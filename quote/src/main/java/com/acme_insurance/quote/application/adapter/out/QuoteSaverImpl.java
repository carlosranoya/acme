package com.acme_insurance.quote.application.adapter.out;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.acme_insurance.quote.application.dto.QuoteData;
import com.acme_insurance.quote.application.repository.QuoteRepository;
import com.acme_insurance.quote.ports.dto.Response;
import com.acme_insurance.quote.ports.out.QuoteSaverInterface;
import com.acme_insurance.quote.ports.out.SaveQuoteRequestInterface;
import com.acme_insurance.quote.ports.out.SaveQuoteResponseInterface;

@Repository
public class QuoteSaverImpl implements QuoteSaverInterface {

    @Autowired
    private QuoteRepository quoteRepository;

    @Override
    public SaveQuoteResponseInterface save(SaveQuoteRequestInterface request) {
        try {
            quoteRepository.save( (QuoteData) request );

            return new SaveQuoteResponseInterface() {
                @Override
                public List<Map<String, Object>> errors() {
                    return null;
                }
                @Override
                public Long id() {
                    return request.id();
                }         
            };
        } catch (Exception e) {
            return new SaveQuoteResponseInterface() {
                @Override
                public List<Map<String, Object>> errors() {
                    return Response.getErrorList(e);
                }
                @Override
                public Long id() {
                    return request.id();
                }         
            };
        }
    }

}
