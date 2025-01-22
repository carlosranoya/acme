package com.acme_insurance.quote.infra.auth;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.acme_insurance.quote.infra.ports.RequestAuthenticator;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

@Service
public class RequestAuthenticatorImpl extends Authenticator implements RequestAuthenticator {

    @Value("${environment.auth_service.url}")
    private String url;

    private HttpClient client = HttpClient.newHttpClient();

    public RequestAuthenticatorImpl(String url) {
        this.url = url;
    }
    public RequestAuthenticatorImpl() {
    }

    @Override
    public Result authenticate(HttpExchange exch) {
        String access_token = exch.getRequestHeaders().getFirst("access_token");
        
        String body = access_token == null ?
        """
            { "access_token": null }
            
        """ :
        String.format(
                """
                { "access_token": "%s" }
            
                """, 
                access_token);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .header("Content-Type", "application/json")
            .build();
        
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() < 300) {
                return new Authenticator.Success(new HttpPrincipal("", ""));
            } else {
                return new Authenticator.Failure(response.statusCode());
            }
        } catch (IOException e) {        
            return new Authenticator.Failure(500);
        } catch (InterruptedException e) {
            return new Authenticator.Failure(500);
        }

    }

    @Override
    public Authenticator getAuthenticator() {
        return this;
    }

}

class CustomResult extends Authenticator.Result {

}
