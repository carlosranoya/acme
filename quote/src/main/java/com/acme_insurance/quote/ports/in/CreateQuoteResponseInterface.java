package com.acme_insurance.quote.ports.in;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.ports.ErrorInterface;

public interface CreateQuoteResponseInterface extends ErrorInterface {

    Long id();
    String productId();
    String offerId(); 
    String category(); 
    BigDecimal totalMonthlyPremiumAmount();
    BigDecimal totalCoverageAmount();
    Map<String, BigDecimal>  coverages();
    List<String> assistances();
    Customer customer();

}
