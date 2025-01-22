package com.acme_insurance.quote.ports.out;

import com.acme_insurance.quote.domain.value_object.Product;
import com.acme_insurance.quote.ports.dto.Response;


public interface ProductGetterInterface {

    Response<Product> getProduct(String id);

}
