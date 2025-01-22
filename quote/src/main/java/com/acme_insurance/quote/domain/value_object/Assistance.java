package com.acme_insurance.quote.domain.value_object;

public enum Assistance {

    ENCANADOR("Encanador"),
    ELETRICISTA("Eletricista"),
    CHAVEIRO_24H("Chaveiro 24h"),
    GUINCHO_24H("Guincho 24h");

    private String description;

    private Assistance(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Assistance fromDescription(String descr) {
        for (Assistance a : Assistance.values()) {
            if (a.getDescription().equals(descr)) {
                return a;
            }
        }
        return null;
    }

}
