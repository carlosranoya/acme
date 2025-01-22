package com.acme_insurance.quote.application.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.acme_insurance.quote.application.dto.QuoteData;

@Repository
public interface QuoteRepository extends
    CustomQuoteRepository,
    MongoRepository<QuoteData, ObjectId> {  

}
