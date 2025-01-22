package com.acme_insurance.quote.infra.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.acme_insurance.quote.infra.controller.QuoteHandler;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


@Component
public class HTTPServer {

    private static final Logger logger = Logger.getLogger(HTTPServer.class.getName());

    private static HttpServer server;
    private int port;

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private QuoteHandler quoteHandler;

    public HTTPServer(
        @Value("${environment.port}") int port
    ) {
         this.port = port;
    }

    private void config() throws IOException {
        if (server == null) {
            
            server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            
            HttpContext context = server.createContext("/quote", quoteHandler);
            context.setAuthenticator(authenticator);
            server.createContext("/", new HttpHandler(){
                @Override
                            public
                            void handle(final HttpExchange httpExchange) throws IOException {
                                OutputStream outputStream = httpExchange.getResponseBody();
                                httpExchange.getResponseHeaders().set("Content-Type", "application/json");  
                                String message = "Resource not found";
                                httpExchange.sendResponseHeaders(404, message.length());
                                outputStream.write(message.getBytes(StandardCharsets.UTF_8));                       
                                outputStream.flush();
                                outputStream.close();
                            }
            });

            server.setExecutor((ExecutorService)Executors.newSingleThreadExecutor());    
        }
    }

    public void startServer() throws IOException {
        config();
        logStartMessage();
        server.start();
    }

    public void stopServer() {
        logStopMessage();
        server.stop(0);
    }

    private void logStartMessage() {
        logger.info(String.format("HTTP Server started on port %d", port));
    }

    private void logStopMessage() {
        logger.info("HTTP Server stopped");
    }

}
