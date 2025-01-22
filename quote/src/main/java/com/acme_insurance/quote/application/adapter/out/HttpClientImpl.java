package com.acme_insurance.quote.application.adapter.out;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.stereotype.Repository;

@Repository
public class HttpClientImpl implements HttpClientInterface {

    private Integer status;
    private String body;
    private HttpClient client = HttpClient.newHttpClient();

    @Override
    public void fetch(String url) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        HttpResponse<String> response;
        
        response = client.send(request, BodyHandlers.ofString());

        status = response.statusCode();
        body = response.body();

    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getBody() {
        return body;
    }



}
