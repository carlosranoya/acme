package com.acme_insurance.quote.ports.in;

import java.time.LocalDateTime;

public interface RetrieveQuoteResponseInterface extends CreateQuoteResponseInterface {

    Long insurancePolicyId();
    LocalDateTime createdAt();
    LocalDateTime updatedAt();
    
}
