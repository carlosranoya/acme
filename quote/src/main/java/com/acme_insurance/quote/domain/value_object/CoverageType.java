package com.acme_insurance.quote.domain.value_object;

public enum CoverageType {

    FIRE("Incêndio"),
    NATURAL_DISASTERS("Desastres naturais"),
    CIVIL_LIABILITY("Responsabiliadade civil"),
    ROBBERY("Roubo"),
    THEFT("Furto");


    private String description;

    private CoverageType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}