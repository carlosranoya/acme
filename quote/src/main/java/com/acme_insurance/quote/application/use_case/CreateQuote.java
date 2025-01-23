package com.acme_insurance.quote.application.use_case;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme_insurance.quote.application.adapter.in.dto.CreateQuoteErrorResponseImpl;
import com.acme_insurance.quote.application.adapter.in.dto.RetrieveQuoteErrorResponseImpl;
import com.acme_insurance.quote.application.dto.QuoteData;
import com.acme_insurance.quote.domain.entity.IdGenerator;
import com.acme_insurance.quote.domain.entity.Quote;
import com.acme_insurance.quote.domain.value_object.Assistance;
import com.acme_insurance.quote.domain.value_object.InvalidationReason;
import com.acme_insurance.quote.infra.broker.RabbitMQHandler;
import com.acme_insurance.quote.ports.common.error.SystemError;
import com.acme_insurance.quote.ports.in.CreateQuoteRequestInterface;
import com.acme_insurance.quote.ports.in.CreateQuoteResponseInterface;
import com.acme_insurance.quote.ports.in.QuoteCreator;
import com.acme_insurance.quote.ports.out.OfferGetterInterface;
import com.acme_insurance.quote.ports.out.ProductGetterInterface;
import com.acme_insurance.quote.ports.out.QuoteSaverInterface;
import com.acme_insurance.quote.ports.out.SaveQuoteResponseInterface;

@Service
public class CreateQuote implements QuoteCreator {

    // TODO: Esta classe não pode depender diretamente de um recurso de infraestrutura
    // criar interface para posterior inversão de controle
    @Autowired
    private RabbitMQHandler brokerService;

    @Autowired
    private QuoteSaverInterface quoteSaver;

    @Autowired
    private OfferGetterInterface offerGetter;

    @Autowired
    private ProductGetterInterface productGetter;

    @Override
    public CreateQuoteResponseInterface create(CreateQuoteRequestInterface request) {

        List<Assistance> listAssistance = new ArrayList<>();

        for (String a : request.assistances()) {
            Assistance assistance = Assistance.fromDescription(a);

            // invalid assistance ---> return error
            if (assistance == null) {
                List<Map<String, Object>> errors = new ArrayList<>();
                Map<String, Object> error = new HashMap<>();
                error.put("code", InvalidationReason.INVALID_ASSISTANCE.getCode());
                error.put("message", InvalidationReason.INVALID_ASSISTANCE.getDescription());
                errors.add(error);
                return RetrieveQuoteErrorResponseImpl.asError(errors);
            }
            
            listAssistance.add(Assistance.fromDescription(a));

        }

        Quote quote = Quote.create(
            request.productId(),
            request.offerId(),
            request.category(),
            request.totalMonthlyPremiumAmount(),
            request.totalCoverageAmount(),
            request.coverages(),
            listAssistance,
            request.customer()
        );

        // checking invalid data
        List<InvalidationReason> invalidations;
        try {
            invalidations = quote.validate(productGetter, offerGetter);
        } catch (SystemError e) {
            return RetrieveQuoteErrorResponseImpl.asError(e.errors());
        }

        // return error if invalidated data found
        if (invalidations.size() > 0) {
            List<Map<String, Object>> errors = new ArrayList<>();

            for (InvalidationReason reason : invalidations) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", reason.getCode());
                error.put("message", reason.getDescription());
                errors.add(error);
            }
            return RetrieveQuoteErrorResponseImpl.asError(errors);
        }

        LocalDateTime now = LocalDateTime.now();
        Long customId = IdGenerator.generate();

        SaveQuoteResponseInterface response = quoteSaver.save(
            new QuoteData(customId, null, 
            request.productId(), request.offerId(),request.category(), now, now, 
            request.totalMonthlyPremiumAmount(), request.totalCoverageAmount(), 
            request.coverages(), request.assistances(), request.customer())
        );

        List<Map<String, Object>> errors = response.errors();
        if (errors != null && !errors.isEmpty()) {
            return CreateQuoteErrorResponseImpl.asError(errors);
        }

        brokerService.send(response.id().toString(), 1);

        // TODO: if send message fails -----> resilience strategy

        return new CreateQuoteErrorResponseImpl(customId, request.productId(), request.offerId(), 
            request.category(), request.totalMonthlyPremiumAmount(), request.totalCoverageAmount(),
            request.coverages(), request.assistances(), request.customer(), null);

    }

}
