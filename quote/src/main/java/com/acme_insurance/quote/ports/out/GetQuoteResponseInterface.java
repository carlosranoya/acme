package com.acme_insurance.quote.ports.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.ports.common.ErrorInterface;

public interface GetQuoteResponseInterface extends ErrorInterface {

    Long id();
    Long insurancePolicyId();
    String productId();
    String offerId(); 
    String category();
    LocalDateTime createdAt();
    LocalDateTime updatedAt();
    BigDecimal totalMonthlyPremiumAmount();
    BigDecimal totalCoverageAmount();
    Map<String, BigDecimal>  coverages();
    List<String> assistances();
    Customer customer();

}
