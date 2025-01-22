package com.acme_insurance.quote.application.repository;

import com.acme_insurance.quote.application.dto.QuoteData;

public interface CustomQuoteRepository {

    QuoteData findByCustomId(Long customId);

    void updatePolicyByCustomId(Long customId, Long insurancePolicyId);

}
