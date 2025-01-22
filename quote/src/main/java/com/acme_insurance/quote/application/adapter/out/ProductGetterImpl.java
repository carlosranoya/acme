package com.acme_insurance.quote.application.adapter.out;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.acme_insurance.quote.domain.value_object.Product;
import com.acme_insurance.quote.ports.dto.Response;
import com.acme_insurance.quote.ports.out.ProductGetterInterface;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ProductGetterImpl implements ProductGetterInterface {

    private static ObjectMapper mapper = new ObjectMapper();

    @Value("${environment.catalog_service.product_url}")
    private String productServiceUrl;

    @Autowired
    HttpClientInterface client;

    @Override
    public Response<Product> getProduct(String id) {
        try {
            client.fetch(productServiceUrl + id);
            if (client.getBody() == null) {
                return new Response<>(null, null);
            }
            return new Response<>(
                mapper.readValue(client.getBody(), Product.class),
                null
            );
        } catch (IOException | InterruptedException e) {
            return new Response<>(e);
        }

    }

}
