package com.acme_insurance.quote.infra.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class LocalTestServer {

    public static HttpServer create(int port, Map<String, HttpHandler> handlers) throws IOException {

        final HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        if (handlers != null) 
            handlers.forEach((path, handler) -> server.createContext(path, handler));
        server.setExecutor((ExecutorService)Executors.newSingleThreadExecutor());
        return server;

    }

}
