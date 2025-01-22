package com.acme_insurance.quote.infra.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

abstract class HandlerBase implements HttpHandler {   

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HandledData data = null;
        if ("GET".equals(httpExchange.getRequestMethod())) {
            data = handleGetRequest(httpExchange);
        } else if("POST".equals(httpExchange.getRequestMethod())) {
            data = handlePostRequest(httpExchange);
        }
        handleResponse(httpExchange, data);
    }

    abstract protected HandledData handleGetRequest(HttpExchange httpExchange);
    abstract protected HandledData handlePostRequest(HttpExchange httpExchange);

    protected String defaultContentType() {
        return "application/json";
    }

    protected void handleResponse(HttpExchange httpExchange, HandledData data)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        if (data.contentType() != null) {
            httpExchange.getResponseHeaders().set("Content-Type", data.contentType());
        } else {
            if (defaultContentType() != null) {
                httpExchange.getResponseHeaders().set("Content-Type", defaultContentType());
            }
        }

        httpExchange.sendResponseHeaders(data.status(), data.body() == null ? 0 : data.length());
        if (data.body() != null) { 
            outputStream.write(data.body().getBytes(StandardCharsets.UTF_8));
        }
        outputStream.flush();
        outputStream.close();
    }

}

record HandledData (int status, String body, int length, String contentType){}

