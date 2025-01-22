package com.acme_insurance.quote.domain.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.acme_insurance.quote.TestData;
import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.domain.value_object.InvalidationReason;
import com.acme_insurance.quote.domain.value_object.Product;
import com.acme_insurance.quote.ports.common.error.SystemError;
import com.acme_insurance.quote.ports.dto.Response;
import com.acme_insurance.quote.ports.out.OfferGetterInterface;
import com.acme_insurance.quote.ports.out.ProductGetterInterface;

@SpringBootTest
public class QuoteTest {

    

    @Mock
    private ProductGetterInterface productGetter;

    @Mock
    private OfferGetterInterface offerGetter;

    // @InjectMocks
    // private BookService bookService;

    @Test
    public void quoteShouldValidate_whenValidProductAndOffer() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer1.id())))
            .thenReturn(new Response<>(TestData.activeOffer1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer1.id(), "HOME", 
            new BigDecimal(70), TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        List<InvalidationReason> invalidations;
        try {
            invalidations = quote.validate(productGetter, offerGetter);
            assertEquals(invalidations.size(), 0);
        } catch (SystemError e) {
            assertNull(e);
        }
               
    }

    @Test
    public void quoteShouldInvalidate_whenWrongCoverageTotal() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer1.id())))
            .thenReturn(new Response<>(TestData.activeOffer1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer1.id(), "HOME", 
            new BigDecimal(70), TestData.wrongCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.COVERAGE_EXCEEDED_AMOUNT);
        });       

    }

    @Test
    public void quoteShouldInvalidate_whenTotalPremiumAmountExceeded() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer1.id())))
            .thenReturn(new Response<>(TestData.activeOffer1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer1.id(), "HOME", 
            new BigDecimal(200), TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.TOTAL_MONTHLY_PREMIUM_EXCEEDS_MAX);
        });

    }

    @Test
    public void quoteShouldInvalidate_whenTotalPremiumAmountBelowMinimum() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer1.id())))
            .thenReturn(new Response<>(TestData.activeOffer1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer1.id(), "HOME", 
            new BigDecimal(25), TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.TOTAL_MONTHLY_PREMIUM_BELOW_MIN);
        });

    }

    @Test
    public void quoteShouldInvalidate_whenTotalPremiumAmountIsNull() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer1.id())))
            .thenReturn(new Response<>(TestData.activeOffer1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer1.id(), "HOME", 
            null, TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));

        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.TOTAL_MONTHLY_PREMUIM_REQUIRED);
        });
 
    }

    @Test
    public void quoteShouldInvalidate_whenTotalPremiumAmountIsZero() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer1.id())))
            .thenReturn(new Response<>(TestData.activeOffer1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer1.id(), "HOME", 
            BigDecimal.ZERO, TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.TOTAL_MONTHLY_PREMUIM_REQUIRED);
        });
 
    }

    @Test
    public void quoteShouldInvalidate_whenTotalCoverageIsNull() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer1.id())))
            .thenReturn(new Response<>(TestData.activeOffer1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer1.id(), "HOME", 
            new BigDecimal(70), null, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.TOTAL_COVERAGE_REQUIRED);
        });

    }

    @Test
    public void quoteShouldInvalidate_whenTotalCoverageIsZero() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer1.id())))
            .thenReturn(new Response<>(TestData.activeOffer1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer1.id(), "HOME", 
            new BigDecimal(70), BigDecimal.ZERO, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.TOTAL_COVERAGE_REQUIRED);
        });

    }

    @Test
    public void quoteShouldInvalidate_whenProductIdIsNull() {

        Quote quote = Quote.create(
            null, TestData.activeOffer1.id(), "HOME", 
            new BigDecimal(70), TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.PRODUCT_ID_FIELD_REQUIRED);
        });

    }

    @Test
    public void quoteShouldInvalidate_whenProductIsInactive() {

        when(productGetter.getProduct(eq(TestData.inactiveProd.id())))
            .thenReturn(new Response<Product>(TestData.inactiveProd, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer1.id())))
            .thenReturn(new Response<>(TestData.activeOffer1, null));

        Quote quote = Quote.create(
            TestData.inactiveProd.id(), TestData.activeOffer1.id(), "HOME", 
            new BigDecimal(70), TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.INACTIVE_PRODUCT);
        });

    }

    @Test
    public void quoteShouldInvalidate_whenOfferIsInactive() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.inactiveOffer1.id())))
            .thenReturn(new Response<>(TestData.inactiveOffer1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.inactiveOffer1.id(), "HOME", 
            new BigDecimal(70), TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.INACTIVE_OFFER);
        });

    }

    @Test
    public void quoteShouldInvalidate_whenOfferIsNotRelatedToProduct() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer3.id())))
            .thenReturn(new Response<>(TestData.activeOffer3, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer3.id(), "HOME", 
            new BigDecimal(70), TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.INVALID_OFFER);
        });


    }

    @Test
    public void quoteShouldInvalidate_whenOfferIdIsNull() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), null, "HOME", 
            new BigDecimal(70), TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.OFFER_ID_FIELD_REQUIRED);
        });
        
    }

    @Test
    public void quoteShouldInvalidate_whenHasMoreAssistancesThanOffer() {

        when(productGetter.getProduct(eq(TestData.activeProd1.id())))
            .thenReturn(new Response<Product>(TestData.activeProd1, null));
        when(offerGetter.getOffer(eq(TestData.activeOffer2.id())))
            .thenReturn(new Response<>(TestData.activeOffer2, null));

        Quote quote = Quote.create(
            TestData.activeProd1.id(), TestData.activeOffer2.id(), "HOME", 
            new BigDecimal(100), TestData.correctCoverageTotal, TestData.getCoverages(), 
            TestData.allAssistances, new Customer(null, null, null, null, null, null, null));
        
        assertDoesNotThrow(() -> {
            List<InvalidationReason> invalidations = quote.validate(productGetter, offerGetter);

            assertEquals(invalidations.size(), 1);
            assertEquals(invalidations.get(0), InvalidationReason.INVALID_ASSISTANCE);
        });

    }

}
