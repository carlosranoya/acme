package com.acme_insurance.quote.infra.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.acme_insurance.quote.application.adapter.in.dto.CreateQuoteRequestImpl;
import com.acme_insurance.quote.application.adapter.in.dto.RetrieveQuoteRequestImpl;
import com.acme_insurance.quote.application.use_case.CreateQuote;
import com.acme_insurance.quote.application.use_case.RetrieveQuote;
import com.acme_insurance.quote.ports.ErrorInterface;
import com.acme_insurance.quote.ports.in.CreateQuoteResponseInterface;
import com.acme_insurance.quote.ports.in.RetrieveQuoteResponseInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;

@Controller
public class QuoteHandler extends HandlerBase {

    private static ObjectMapper mapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);

    @Autowired
    private CreateQuote createQuoteCase;

    @Autowired
    private RetrieveQuote retrieveQuoteCase;

    @Override
    protected HandledData handleGetRequest(HttpExchange httpExchange) {
        
        try {
            byte[] body = httpExchange.getRequestBody().readAllBytes();
            RetrieveQuoteRequestImpl request = mapper.readValue(body, RetrieveQuoteRequestImpl.class);
            RetrieveQuoteResponseInterface response = retrieveQuoteCase.get(request);
            return generate(response);

        } catch (IOException e) {
            return new HandledData(500, e.getMessage(), 0, "text/plain");
        }       

    }

    @Override
    protected HandledData handlePostRequest(HttpExchange httpExchange) {
        try {
            byte[] body = httpExchange.getRequestBody().readAllBytes();

            CreateQuoteRequestImpl request = mapper.readValue(body, CreateQuoteRequestImpl.class);

            CreateQuoteResponseInterface response = createQuoteCase.create(request);

            if (response.errors() != null) {
                byte[] bytes = mapper.writeValueAsBytes(response.errors());
                for (Map<String, Object> map : response.errors()) {
                    if (map.containsKey("code") && map.get("code").toString().equals("999")) {
                        return new HandledData(
                        500, 
                        new String(bytes, StandardCharsets.UTF_8), 
                        bytes.length, 
                        "application/json");
                    }
                }     
                return new HandledData(
                    422, 
                    new String(bytes, StandardCharsets.UTF_8), 
                    bytes.length, 
                    "application/json");
            } else {
                byte[] bytes = mapper.writeValueAsBytes(response);
                return new HandledData(
                    201, 
                    new String(bytes, StandardCharsets.UTF_8), 
                    bytes.length, 
                    "application/json");
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
            return new HandledData(500, e.getMessage(), 0, "text/plain");
        }

    }

    private HandledData generate(ErrorInterface response) {
        try {
            if (response.errors() != null) {
                byte[] bytes = mapper.writeValueAsBytes(response.errors());
                
                for (Map<String, Object> map : response.errors()) {
                    if (map.containsKey("code") && map.get("code").toString().equals("999")) {
                        return new HandledData(
                        500, 
                        new String(bytes, StandardCharsets.UTF_8), 
                        bytes.length, 
                        "application/json");
                    }
                }     
                return new HandledData(
                    422, 
                    new String(bytes, StandardCharsets.UTF_8), 
                    bytes.length, 
                    "application/json");
            } else {
                byte[] bytes = mapper.writeValueAsBytes(response);
                return new HandledData(
                    201, 
                    new String(bytes, StandardCharsets.UTF_8), 
                    bytes.length, 
                    "application/json");
            }
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return new HandledData(500, e.getMessage(), 0, "text/plain");
        }
    }

    @Override
    protected void handleResponse(HttpExchange httpExchange, HandledData data)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");   
        httpExchange.sendResponseHeaders(data.status(), data.body() == null ? 0 : data.length());
        if (data.body() != null) { 
            outputStream.write(data.body().getBytes(StandardCharsets.UTF_8));
        }
        outputStream.flush();
        outputStream.close();
    }

}