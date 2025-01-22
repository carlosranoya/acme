package com.acme_insurance.quote.ports.out;

import com.acme_insurance.quote.domain.value_object.Offer;
import com.acme_insurance.quote.ports.dto.Response;


public interface OfferGetterInterface {

    Response<Offer> getOffer(String id);

}
