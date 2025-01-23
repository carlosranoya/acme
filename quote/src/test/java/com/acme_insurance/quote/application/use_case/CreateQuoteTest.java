package com.acme_insurance.quote.application.use_case;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.acme_insurance.quote.TestData;
import com.acme_insurance.quote.application.adapter.in.dto.CreateQuoteRequestImpl;
import com.acme_insurance.quote.domain.value_object.Offer;
import com.acme_insurance.quote.domain.value_object.Product;
import com.acme_insurance.quote.infra.broker.RabbitMQHandler;
import com.acme_insurance.quote.ports.dto.Response;
import com.acme_insurance.quote.ports.in.CreateQuoteResponseInterface;
import com.acme_insurance.quote.ports.out.OfferGetterInterface;
import com.acme_insurance.quote.ports.out.ProductGetterInterface;
import com.acme_insurance.quote.ports.out.QuoteSaverInterface;
import com.acme_insurance.quote.ports.out.SaveQuoteResponseInterface;

@SpringBootTest
public class CreateQuoteTest {

    private Long savedId = 13579l;

    private SaveQuoteResponseInterface saveQuoteResponse = new SaveQuoteResponseInterface() {
        @Override
        public List<Map<String, Object>> errors() {
            return null;
        }
        @Override
        public Long id() {
            return savedId;
        }         
    };

    private CreateQuoteRequestImpl request1 = new CreateQuoteRequestImpl(
        TestData.activeProd1.id(),
        TestData.activeOffer1.id(),
        "Home",
        new BigDecimal(70),
        TestData.correctCoverageTotal,
        TestData.getCoverages(),
        TestData.parsedSomeAssiatances,
        TestData.emptyCustumer
    );



    @Mock
    private RabbitMQHandler brokerService;

    @Mock
    private QuoteSaverInterface quoteSaver;

    @Mock
    private OfferGetterInterface offerGetter;

    @Mock
    private ProductGetterInterface productGetter;

    @InjectMocks
    private CreateQuote quoteCrateor;

    @Test
    public void testQuoteCreatorHappyPath() {

        when(productGetter.getProduct(TestData.activeProd1.id()))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(TestData.activeOffer1.id()))
            .thenReturn(new Response<Offer>(TestData.activeOffer1, null));
        when(quoteSaver.save(any()))
            .thenReturn(saveQuoteResponse);
        doNothing().when(brokerService).send(savedId.toString(), 1);

        CreateQuoteResponseInterface response = quoteCrateor.create(request1);

        assertEquals(response.offerId(), TestData.activeOffer1.id());
    }




}
