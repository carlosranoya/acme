package com.acme_insurance.quote.infra.server.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.acme_insurance.quote.infra.auth.RequestAuthenticatorImpl;
import com.acme_insurance.quote.infra.server.LocalTestServer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;


@SpringBootTest
public class RequestAuthenticatorImplTest {

    @Value("${environment.auth_service.url}")
    private String authUrl;

    private List<String> validTokens = new ArrayList<>(Arrays.asList(new String[] {
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBY21lIEF1dGgiLCJpYXQiOjE3MzczMTM4NjksImV4cCI6MTc2ODg0OTg2OSwiYXVkIjoid3d3LmFjbWUuY29tIiwic3ViIjoianJvY2tldEBhY21lLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAYWNtZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.jGvMFywDJNL12JuEaXSfnQpwp60o79RXj0aOh1WC6CI",
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBY21lIEF1dGgiLCJpYXQiOjE3MzczMTM4NjksImV4cCI6MTc2ODg0OTg2OSwiYXVkIjoid3d3LmFjbWUuY29tIiwic3ViIjoibWFndWlsYUBhY21lLmNvbSIsIkdpdmVuTmFtZSI6Ik1hZ3VpbGEiLCJTdXJuYW1lIjoiUm9kcmlndWVzIiwiRW1haWwiOiJtYWd1aWxhQGFjbWUuY29tIiwiUm9sZSI6IkNsaWVudCJ9.HcOhnY_4YYpJvSOGBAq-xPcASn4NY-QZrXAKsHB5D5s",
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBY21lIEF1dGgiLCJpYXQiOjE3MzczMTM4NjksImV4cCI6MTc2ODg0OTg2OSwiYXVkIjoid3d3LmFjbWUuY29tIiwic3ViIjoiY2FuZXRhLmF6dWxAYWNtZS5jb20iLCJHaXZlbk5hbWUiOiJDYW5ldGEiLCJTdXJuYW1lIjoiQXp1bCIsIkVtYWlsIjoiY2FuZXRhLmF6dWxAYWNtZS5jb20iLCJSb2xlIjoiQ2xpZW50In0.qOmVY1r9wSod4h0-ur8v2v8K1tkDQ1FHmAOIBcIWhqI"
    }));

    private HttpServer getLocalServer() throws IOException {
        
        Map<String, HttpHandler> handlers = new HashMap<>();
        handlers.put("/api/auth/validate", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

                byte[] bodyData = exchange.getRequestBody().readAllBytes();

                if (bodyData.length == 0) {
                    exchange.sendResponseHeaders(422, 0);
                    exchange.close();
                    return;
                }

                ObjectMapper mapper = new ObjectMapper(); 
  
                TypeReference<HashMap<String,Object>> typeRef 
                        = new TypeReference<HashMap<String,Object>>() {};
                try {
                    HashMap<String,Object> body = mapper.readValue(
                        new String(bodyData, StandardCharsets.UTF_8), 
                        typeRef);
                    if (body.containsKey("access_token")) {

                        String token = body.get("access_token").toString();
                        if (token == null) {
                            exchange.sendResponseHeaders(422, 0);
                        }
                        if (validTokens.indexOf(token) >= 0) {
                            exchange.sendResponseHeaders(204, 0);
                        } else {
                            exchange.sendResponseHeaders(403, 0);
                        }

                    } else {
                        exchange.sendResponseHeaders(422, 0);
                    }
                } catch (Exception e) {
                    exchange.sendResponseHeaders(422, 0);
                }
                exchange.close();
                
            }
        });
        
        return LocalTestServer.create(3005, handlers);
          
    }

    @Test
    public void whenExchangeCreatedWithHeaders_shouldRetrieveTheSameHeaderValue() {
        Headers headers = new Headers();
        headers.add("access_token", validTokens.get(0));
        try (HttpExchange exchange = new CustomHttpExchange(headers)) {
            assertEquals(
                validTokens.get(0), 
                exchange.getRequestHeaders().getFirst("access_token"));
        }
    }

    @Test
    public void whenAuthenticatorSendValidToken_shouldReturnOk() {
        
        assertDoesNotThrow(() -> {

            HttpServer server = getLocalServer();
            server.start();

            RequestAuthenticatorImpl authenticator = new RequestAuthenticatorImpl("http://localhost:3005/api/auth/validate");

            Headers headers = new Headers();
            headers.add("access_token", validTokens.get(1));
            HttpExchange exchange = new CustomHttpExchange(headers);

            Authenticator.Result result = authenticator.authenticate(exchange);
            server.stop(0);

            assertInstanceOf(Authenticator.Success.class, result);
        });

    }


    @Test
    public void whenAuthenticatorWithWrongUrl_shouldReturn404() {
        
        assertDoesNotThrow(() -> {

            HttpServer server = getLocalServer();
            server.start();

            RequestAuthenticatorImpl authenticator = new RequestAuthenticatorImpl("http://localhost:3001/api/auth/wrong");

            Headers headers = new Headers();
            headers.add("access_token", validTokens.get(0));
            HttpExchange exchange = new CustomHttpExchange(headers);

            Authenticator.Result result = authenticator.authenticate(exchange);
            server.stop(0);

            assertInstanceOf(Authenticator.Failure.class, result);

            Authenticator.Failure failure = (Authenticator.Failure) result;

            assertEquals(failure.getResponseCode(), 404);
        });

    }

    @Test
    public void whenAuthenticatorReceivesNoAcessToken_shoeldReturn422() {
        
        assertDoesNotThrow(() -> {

            HttpServer server = getLocalServer();
            server.start();

            RequestAuthenticatorImpl authenticator = new RequestAuthenticatorImpl(authUrl);

            Headers headers = new Headers();

            HttpExchange exchange = new CustomHttpExchange(headers);

            Authenticator.Result result = authenticator.authenticate(exchange);
            server.stop(0);

            assertInstanceOf(Authenticator.Failure.class, result);

            Authenticator.Failure failure = (Authenticator.Failure) result;

            assertEquals(failure.getResponseCode(), 422);
        });

    }

    @Test
    public void whenAuthenticatorSendInvalidToken_shouldReturn403() {
        
        assertDoesNotThrow(() -> {
            HttpServer server = getLocalServer();
            server.start();

            RequestAuthenticatorImpl authenticator = new RequestAuthenticatorImpl(authUrl);

            Headers headers = new Headers();
            headers.add("access_token", "invalid_token");
            HttpExchange exchange = new CustomHttpExchange(headers);

            Authenticator.Result result = authenticator.authenticate(exchange);
            server.stop(0);

            assertInstanceOf(Authenticator.Failure.class, result);

            Authenticator.Failure failure = (Authenticator.Failure) result;
            assertEquals(failure.getResponseCode(), 403);
        });

    }

}

class CustomHttpExchange extends HttpExchange {

    private Headers requestHeaders;
    private Headers responseHeaders = new Headers();

    public CustomHttpExchange(Headers requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    @Override
    public Headers getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public URI getRequestURI() {
        return null;
    }

    @Override
    public String getRequestMethod() {
        return "POST";
    }

    @Override
    public HttpContext getHttpContext() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public InputStream getRequestBody() {
        return null;
    }

    @Override
    public OutputStream getResponseBody() {
        return null;
    }

    @Override
    public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public int getResponseCode() {
        return 0;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public String getProtocol() {
        return "http";
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {

    }

    @Override
    public void setStreams(InputStream i, OutputStream o) {

    }

    @Override
    public HttpPrincipal getPrincipal() {
        return null;
    }
    
}