package com.acme_insurance.quote.application.repository;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.acme_insurance.quote.application.dto.QuoteData;

public class CustomQuoteRepositoryImpl implements CustomQuoteRepository {

    private final MongoTemplate mongoTemplate;

    public CustomQuoteRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate= mongoTemplate;
    }

    @Override
    public QuoteData findByCustomId(Long customId) {
        Query query = new Query()
            .addCriteria(Criteria.where("custom_id").is(customId))
            .limit(1);

            QuoteData result = this.mongoTemplate.findOne(query, QuoteData.class);
            return result;
    }

    @Override
    public void updatePolicyByCustomId(Long customId, Long insurancePolicyId) {
        Query query = new Query()
            .addCriteria(Criteria.where("custom_id").is(customId))
            .limit(1);
        Update update = new Update().set("insurance_policy_id", insurancePolicyId);
        this.mongoTemplate.upsert(query, update, QuoteData.class);  
    }

}
