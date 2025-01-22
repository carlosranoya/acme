package com.acme_insurance.quote;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.acme_insurance.quote.application.repository.QuoteRepository;
import com.acme_insurance.quote.domain.value_object.Assistance;
import com.acme_insurance.quote.domain.value_object.CoverageType;
import com.acme_insurance.quote.domain.value_object.MonthlyPremiumAmount;
import com.acme_insurance.quote.domain.value_object.Offer;
import com.acme_insurance.quote.domain.value_object.Product;
import com.acme_insurance.quote.infra.server.HTTPServer;
import com.acme_insurance.quote.ports.Response;
import com.acme_insurance.quote.ports.out.OfferGetterInterface;
import com.acme_insurance.quote.ports.out.ProductGetterInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@SpringBootApplication
public class QuoteApplication {

	@Value("${spring.data.mongodb.port}")
	private int port;

	public static void main(String[] args) {

		SpringApplication.run(QuoteApplication.class, args);

        HTTPServer httpServer = StaticContextAccessor.getBean(HTTPServer.class);

		try {
            httpServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }


	}

    String offerString = """
        {
            "active": true,
            "assistances": [
                "Eletricista",
                "Chaveiro 24h",
                "Assistência Funerária"
            ],
            "coverages": {
                "Desastres naturais": 600000.0,
                "Incêndio": 500000.0,
                "Responsabiliadade civil": 80000.0,
                "Roubo": 100000.0
            },
            "created_at": "2021-07-01T00:00:00Z",
            "id": "adc56d77-348c-4bf0-908f-22d402ee715c",
            "monthly_premium_amount": {
                "max_amount": 100.74,
                "min_amount": 50.0,
                "suggested_amount": 60.25
            },
            "name": "Seguro de Vida Familiar",
            "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587"
        }  
            """;

    private static List<Assistance> someAssiatances = Arrays.asList(new Assistance[] {
        Assistance.ENCANADOR,
        Assistance.ELETRICISTA
    });

    private static List<Assistance> allAssistances = Arrays.asList(new Assistance[] {
        Assistance.ENCANADOR,
        Assistance.ELETRICISTA,
        Assistance.GUINCHO_24H,
        Assistance.CHAVEIRO_24H
    });

    private static Map<String, BigDecimal> getCoverages() {
        Map<String, BigDecimal> map = new HashMap<>();
        map.put(CoverageType.FIRE.getDescription(), new BigDecimal(50000));
        map.put(CoverageType.NATURAL_DISASTERS.getDescription(), new BigDecimal(60000));
        map.put(CoverageType.CIVIL_LIABILITY.getDescription(), new BigDecimal(80000));
        return map;
    }

    private static BigDecimal correctCoverageTotal = new BigDecimal(190000);
    private static BigDecimal wrongCoverageTotal = new BigDecimal(250000);

    private static Map<String, BigDecimal> emptyCoverages() {
        return new HashMap<>();
    }

    private static MonthlyPremiumAmount monthlyPremium1 = new MonthlyPremiumAmount(
        new BigDecimal(100), new BigDecimal(50), new BigDecimal(70));
    private static MonthlyPremiumAmount monthlyPremium2 = new MonthlyPremiumAmount(
        new BigDecimal(160), new BigDecimal(75), new BigDecimal(120));

    private static Offer activeOffer1 = new Offer("11111111-2222-3333-4444-555555555555", 
        "66666666-7777-8888-9999-000000000000", 
        "Offer active 1", LocalDateTime.now(), 
        true, getCoverages(), allAssistances, monthlyPremium1);
    private static Offer inactiveOffer1 = new Offer("22222222-2222-3333-4444-555555555555", 
        "66666666-7777-8888-9999-000000000000", 
        "Offer inactive 1", LocalDateTime.now(), 
        false, getCoverages(), allAssistances, monthlyPremium1);
    private static Offer activeOffer2 = new Offer("77777777-2222-3333-4444-555555555555", 
        "66666666-7777-8888-9999-000000000000", 
        "Offer active 2", LocalDateTime.now(), 
        true, getCoverages(), someAssiatances, monthlyPremium2);
    private static Offer activeOffer3 = new Offer("99999999-2222-3333-4444-555555555555", 
        "66666666-7777-8888-9999-000000000000", 
        "Offer inactive 2", LocalDateTime.now(), 
        true, getCoverages(), allAssistances, monthlyPremium1);

    private static Product activeProd1 = new Product(
        "66666666-7777-8888-9999-000000000000", 
        "Product active", LocalDateTime.now(), true, 
        Arrays.asList(new String[]{
            "11111111-2222-3333-4444-555555555555",
            "22222222-2222-3333-4444-555555555555",
            "77777777-2222-3333-4444-555555555555"
        })
    );

    private static Product activeProd2 = new Product(
        "66666666-7777-8888-9999-000000000000", 
        "Product active", LocalDateTime.now(), true, 
        Arrays.asList(new String[]{
            "11111111-2222-3333-4444-555555555555",
            "99999999-2222-3333-4444-555555555555"
        })
    );

    private static Product inactiveProd = new Product(
        "66666666-7777-8888-9999-000000000000", 
        "Product inactive", LocalDateTime.now(), false,
        Arrays.asList(new String[]{
            "11111111-2222-3333-4444-555555555555",
            "77777777-2222-3333-4444-555555555555",
            "99999999-2222-3333-4444-555555555555"
        })
    );

    static ObjectMapper mapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    class CustomProductGetter implements ProductGetterInterface {

        @Override
        public Response<Product> getProduct(String id) {
            return new Response<>(QuoteApplication.activeProd1, null);
        }
        
    }

    class CustomOfferGetter implements OfferGetterInterface {

        @Override
        public Response<Offer> getOffer(String id) {
            return new Response<>(QuoteApplication.activeOffer2, null);
        }
        
    }

	@Bean
	CommandLineRunner runner (QuoteRepository repository) {

		return args -> {

            Offer offer = mapper.readValue(offerString, Offer.class);
            System.out.println(offer.toString());
        // Quote quote = Quote.create(
        //     activeProd1.id(), activeOffer2.id(), "HOME", 
        //     new BigDecimal(100), correctCoverageTotal, getCoverages(), 
        //     allAssistances, new Customer(null, null, null, null, null, null, null));
        
        // List<InvalidationReason> invalidations = quote.validate(new CustomProductGetter(), new CustomOfferGetter());

            // Quote quote = Quote.create(
            //     activeProd1.id(), activeOffer1.id(), "HOME", 
            //     new BigDecimal(70), wrongCoverageTotal, getCoverages(), 
            //     allAssistances, new Customer(null, null, null, null, null, null, null));
        
            // List<InvalidationReason> invalidations = quote.validate(new CustomProductGetter(), new CustomOfferGetter());
            // ProductGetterImpl prodGetter = new ProductGetterImpl();
            // Response<Product> r = prodGetter.getProduct("2b2da7cc-b367-4196-8a78-9cfeec21f587");

            System.out.println(mapper.writeValueAsString(activeProd1));
            System.out.println(mapper.writeValueAsString(activeOffer1));
            
            System.out.println(mapper.writeValueAsString(getCoverages()));
            System.out.println(mapper.writeValueAsString(allAssistances));
            System.out.println(mapper.writeValueAsString(allAssistances));
            
            
		};
	};

}



