package com.acme_insurance.quote;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.acme_insurance.quote.application.repository.QuoteRepository;
import com.acme_insurance.quote.infra.server.HTTPServer;
import com.acme_insurance.quote.utils.StaticContextAccessor;

import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;


@SpringBootApplication
public class QuoteApplication {

	@Value("${spring.data.mongodb.port}")
	private int port;

	public static void main(String[] args) {

        JvmMetrics.builder().register();

		SpringApplication.run(QuoteApplication.class, args);

        HTTPServer httpServer = StaticContextAccessor.getBean(HTTPServer.class);
        
		try {
            httpServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }

        try {
            // para garantir o bom funcionamento do agente do Prometheus com o servidor Http
            Thread.currentThread().join();
        } catch (InterruptedException e) {
        }
	}

    @Component
    class PropertiesInitializationTest {
        @Value("${spring.data.mongodb.authentication-database}")
        public String database;

        @Value("${spring.data.mongodb.host}")
        public String mongoHost;

        @Value("${environment.auth_service.url}")
        public String authUrl;

        @Value("${environment.catalog_service.product_url}")
        public String productEndpoint;

        @Value("${environment.catalog_service.offer_url}")
        public String offerEndpoint;

        @Value("${environment.rabbitmq_host}")
        public String rabbitHost;

        @Value("${environment.rabbitmq_port}")
        public String rabbitPort;
    }

    @Bean
	CommandLineRunner runner (QuoteRepository repository) {
        
		return args -> {

            PropertiesInitializationTest prop  = StaticContextAccessor.getBean(PropertiesInitializationTest.class);

            System.out.println("----- RabbitMQ Host: " + prop.rabbitHost);
            System.out.println("----- RabbitMQ Port: " + prop.rabbitPort);
            System.out.println("----- Database: " + prop.database);
            System.out.println("----- Mongo Host: " + prop.mongoHost);
            System.out.println("----- Product Endpoint: " + prop.productEndpoint);
            System.out.println("----- Offer Endpoint: " + prop.offerEndpoint);
            System.out.println("----- Auth URL: " + prop.authUrl);
        };

    }
}



