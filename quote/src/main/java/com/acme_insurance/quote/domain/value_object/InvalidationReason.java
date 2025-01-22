package com.acme_insurance.quote.domain.value_object;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum InvalidationReason {

    PRODUCT_NOT_FOUND("Product does not exist", 10),
    OFFER_NOT_FOUND("Offer does not exist", 20),
    INACTIVE_PRODUCT("Inactive product", 30),
    INACTIVE_OFFER("Inactive offer", 40),
    INVALID_OFFER("Offer not related to product", 45),
    INVALID_COVERAGE_TYPE("Coverage is not related to offer", 50),
    COVERAGE_EXCEEDED_AMOUNT("Coverage amount exceeds maximum amount", 60),
    WRONG_TOTAL_COVERAGE_AMOUNT("Wrong total coverge amount", 70),
    TOTAL_MONTHLY_PREMIUM_EXCEEDS_MAX("Total monthly premium exceeds maximum amount", 80),
    TOTAL_MONTHLY_PREMIUM_BELOW_MIN("Total monthly premium is below minimum amount", 90),
    PRODUCT_ID_FIELD_REQUIRED("'product_id' field is required", 100),
    OFFER_ID_FIELD_REQUIRED("'offer_id' field is required", 110),
    COVERAGES_IS_EMPTY_OR_NULL("'coverages' field must contain some coverage information", 120),
    TOTAL_MONTHLY_PREMUIM_REQUIRED("'total_monthly_premium_amount' field is required", 130),
    TOTAL_COVERAGE_REQUIRED("'total_coverage_amount' field is required", 140),
    INVALID_ASSISTANCE("Some required assistance is not covered by offer", 150);

    private String description;
    private int code;

    private InvalidationReason(String description, int code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }
    public int getCode() {
        return code;
    }

}
