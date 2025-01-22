package com.acme_insurance.quote.application.adapter.out;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.acme_insurance.quote.domain.value_object.Offer;
import com.acme_insurance.quote.ports.Response;
import com.acme_insurance.quote.ports.out.OfferGetterInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class OfferGetterImpl implements OfferGetterInterface {

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    HttpClientInterface client;

    @Value("${environment.catalog_service.offer_url}")
    private String offerServiceUrl;

    @Override
    public Response<Offer> getOffer(String id) {
        try {
            client.fetch(offerServiceUrl + id);
            if (client.getBody() == null) {
                return new Response<Offer>(null, null);
            }
            return new Response<>(
                mapper.readValue(client.getBody(), Offer.class),
                null
            );
        } catch (IOException | InterruptedException e) {
            return new Response<Offer>(e);
        }
    }

}
