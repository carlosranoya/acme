package com.acme_insurance.quote.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.ports.in.RetrieveQuoteResponseInterface;
import com.acme_insurance.quote.ports.out.GetQuoteResponseInterface;
import com.acme_insurance.quote.ports.out.SaveQuoteRequestInterface;
import com.acme_insurance.quote.utils.LocalDateTimeDeserializer;
import com.acme_insurance.quote.utils.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Document
@JsonPropertyOrder({ "id", "insurance_policy_id" })
public class QuoteData implements SaveQuoteRequestInterface, RetrieveQuoteResponseInterface, GetQuoteResponseInterface {

    @Id
    @Field("_id")
    private ObjectId _id;

    @Indexed(unique = true)
    @Field("custom_id")
    @JsonProperty("id")
    private Long custom_id;

    @Field("insurance_policy_id")
    @JsonProperty
    private Long insurance_policy_id;

    @Field("product_id")
    @JsonProperty
    private String product_id;

    @Field("offer_id")
    @JsonProperty
    private String offer_id;

    @Field("category")
    @JsonProperty
    private String category;

    @Field("created_at")
    @JsonProperty
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created_at;

    @Field("updated_at")
    @JsonProperty
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updated_at;

    @Field("total_monthly_premium_amount")
    @JsonProperty
    private BigDecimal total_monthly_premium_amount;

    @Field("total_coverage_amount")
    @JsonProperty
    private BigDecimal total_coverage_amount;

    @Field("coverages")
    @JsonProperty
    private Map<String, BigDecimal> coverages;

    @Field("assistances")
    @JsonProperty
    private List<String> assistances;

    @Field("customer")
    @JsonProperty
    private Customer customer;

    @Transient
    private List<Map<String, Object>> _errors;

    @PersistenceCreator
    public  QuoteData(
            ObjectId _id,
            Long custom_id, 
            Long insurance_policy_id, 
            String product_id, 
            String offer_id, 
            String category,
            LocalDateTime created_at, 
            LocalDateTime updated_at, 
            BigDecimal total_monthly_premium_amount, 
            BigDecimal total_coverage_amount, 
            Map<String, BigDecimal> coverages, 
        List<String> assistances, Customer customer) {
        this._id = _id;    
        this.custom_id = custom_id;
        this.insurance_policy_id = insurance_policy_id;
        this.product_id = product_id;
        this.offer_id = offer_id;
        this.category = category;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.total_monthly_premium_amount = total_monthly_premium_amount;
        this.total_coverage_amount = total_coverage_amount;
        this.coverages = coverages;
        this.assistances = assistances;
        this.customer = customer;
    }
    
    public QuoteData(
            Long customId, 
            Long insurancePolicyId, 
            String productId, 
            String offerId, 
            String category,
            LocalDateTime createdAt, 
            LocalDateTime updatedAt, 
            BigDecimal totalMonthlyPremiumAmount, 
            BigDecimal totalCoverageAmount, 
            Map<String, BigDecimal> coverages, 
        List<String> assistances, Customer customer) {
        this.custom_id = customId;
        this.insurance_policy_id = insurancePolicyId;
        this.product_id = productId;
        this.offer_id = offerId;
        this.category = category;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
        this.total_monthly_premium_amount = totalMonthlyPremiumAmount;
        this.total_coverage_amount = totalCoverageAmount;
        this.coverages = coverages;
        this.assistances = assistances;
        this.customer = customer;
    }

    @Transient
    @Override
    public Long id() {
        return custom_id;
    }

    @Transient
    @Override
    public Long insurancePolicyId() {
        return insurance_policy_id;
    }

    @Transient
    @Override
    public String productId() {
        return product_id;
    }

    @Transient
    @Override
    public String offerId() {
        return offer_id;
    }

  
    @Override
    public String category() {
        return category;
    }

    @Transient
    @Override
    public LocalDateTime createdAt() {
        return created_at;
    }

    @Transient
    @Override
    public LocalDateTime updatedAt() {
        return updated_at;
    }

    @Transient
    @Override
    public BigDecimal totalMonthlyPremiumAmount() {
        return total_monthly_premium_amount;
    }

    @Transient
    @Override
    public BigDecimal totalCoverageAmount() {
        return total_coverage_amount;
    }


    @Override
    public Map<String, BigDecimal> coverages() {
        return coverages;
    }

    @Override
    public List<String> assistances() {
        return assistances;
    }

    @Override
    public Customer customer() {
        return customer;
    }

    @Transient
    @Override
    public List<Map<String, Object>> errors() {
        return _errors;
    }

    @Transient
    public void setErrors(List<Map<String, Object>> errors) {
        _errors = errors;
    }

}
