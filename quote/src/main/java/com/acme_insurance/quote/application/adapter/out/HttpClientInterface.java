package com.acme_insurance.quote.application.adapter.out;

import java.io.IOException;

public interface HttpClientInterface {

    void fetch(String url) throws IOException, InterruptedException;
    Integer getStatus();
    String getBody();

}
