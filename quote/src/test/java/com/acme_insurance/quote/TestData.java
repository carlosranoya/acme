package com.acme_insurance.quote;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acme_insurance.quote.domain.value_object.Assistance;
import com.acme_insurance.quote.domain.value_object.CoverageType;
import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.domain.value_object.MonthlyPremiumAmount;
import com.acme_insurance.quote.domain.value_object.Offer;
import com.acme_insurance.quote.domain.value_object.Product;

public class TestData {

    public static List<String> parsedSomeAssiatances = Arrays.asList(new String[] {
        Assistance.ENCANADOR.getDescription(),
        Assistance.ELETRICISTA.getDescription()
    });

    public static List<Assistance> someAssiatances = Arrays.asList(new Assistance[] {
        Assistance.ENCANADOR,
        Assistance.ELETRICISTA
    });

    public static List<Assistance> allAssistances = Arrays.asList(new Assistance[] {
        Assistance.ENCANADOR,
        Assistance.ELETRICISTA,
        Assistance.GUINCHO_24H,
        Assistance.CHAVEIRO_24H
    });

    public static List<Assistance> emptyAssistances = new ArrayList<>();

    public static Map<String, BigDecimal> getCoverages() {
        Map<String, BigDecimal> map = new HashMap<>();
        map.put(CoverageType.FIRE.getDescription(), new BigDecimal(50000));
        map.put(CoverageType.NATURAL_DISASTERS.getDescription(), new BigDecimal(60000));
        map.put(CoverageType.CIVIL_LIABILITY.getDescription(), new BigDecimal(80000));
        return map;
    }

    public static BigDecimal correctCoverageTotal = BigDecimal.ZERO;
    public static BigDecimal wrongCoverageTotal;

    static {
        getCoverages().forEach((c, v) -> {
            correctCoverageTotal = correctCoverageTotal.add(v);
        });

        wrongCoverageTotal = correctCoverageTotal.add(BigDecimal.ONE);
    }

    public static Map<String, BigDecimal> emptyCoverages() {
        return new HashMap<>();
    }

    public static Customer emptyCustumer = new Customer(
        null, null, null, null, null, null, null
    );

    public static MonthlyPremiumAmount monthlyPremium1 = new MonthlyPremiumAmount(
        new BigDecimal(100), new BigDecimal(50), new BigDecimal(70));
    public static MonthlyPremiumAmount monthlyPremium2 = new MonthlyPremiumAmount(
        new BigDecimal(160), new BigDecimal(75), new BigDecimal(120));

    public static Offer activeOffer1 = new Offer("11111111-2222-3333-4444-555555555555", 
        "66666666-7777-8888-9999-000000000000", 
        "Offer active 1", LocalDateTime.now(), 
        true, getCoverages(), allAssistances, monthlyPremium1);
    public static Offer inactiveOffer1 = new Offer("22222222-2222-3333-4444-555555555555", 
        "66666666-7777-8888-9999-000000000000", 
        "Offer inactive 1", LocalDateTime.now(), 
        false, getCoverages(), allAssistances, monthlyPremium1);
    public static Offer activeOffer2 = new Offer("77777777-2222-3333-4444-555555555555", 
        "66666666-7777-8888-9999-000000000000", 
        "Offer active 2", LocalDateTime.now(), 
        true, getCoverages(), someAssiatances, monthlyPremium2);
    public static Offer activeOffer3 = new Offer("99999999-2222-3333-4444-555555555555", 
        "66666666-7777-8888-9999-000000000000", 
        "Offer inactive 2", LocalDateTime.now(), 
        true, getCoverages(), allAssistances, monthlyPremium1);

    public static Product activeProd1 = new Product(
        "66666666-7777-8888-9999-000000000000", 
        "Product active", LocalDateTime.now(), true, 
        Arrays.asList(new String[]{
            "11111111-2222-3333-4444-555555555555",
            "22222222-2222-3333-4444-555555555555",
            "77777777-2222-3333-4444-555555555555"
        })
    );

    public static Product activeProd2 = new Product(
        "66666666-7777-8888-9999-000000000000", 
        "Product active", LocalDateTime.now(), true, 
        Arrays.asList(new String[]{
            "11111111-2222-3333-4444-555555555555",
            "99999999-2222-3333-4444-555555555555"
        })
    );

    public static Product inactiveProd = new Product(
        "66666666-7777-8888-9999-000000000000", 
        "Product inactive", LocalDateTime.now(), false,
        Arrays.asList(new String[]{
            "11111111-2222-3333-4444-555555555555",
            "77777777-2222-3333-4444-555555555555",
            "99999999-2222-3333-4444-555555555555"
        })
    );

}
