package com.acme_insurance.quote.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.acme_insurance.quote.domain.value_object.Assistance;
import com.acme_insurance.quote.domain.value_object.Customer;
import com.acme_insurance.quote.domain.value_object.InvalidationReason;
import com.acme_insurance.quote.domain.value_object.Offer;
import com.acme_insurance.quote.domain.value_object.Product;
import com.acme_insurance.quote.ports.Response;
import com.acme_insurance.quote.ports.SystemError;
import com.acme_insurance.quote.ports.out.OfferGetterInterface;
import com.acme_insurance.quote.ports.out.ProductGetterInterface;

public class Quote {

    private Boolean _isValidated = null;

    private List<InvalidationReason> invalidationReasons;

    private Long id;
    private Long insurancePolicyId;
    private String productId;
    private String offerId;
    private String category;
    private Date createdAt;
    private Date updatedAt;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;
    private Map<String, BigDecimal> coverages;
    private List<Assistance> assistances;
    private Customer customer;

    private Quote(Long id, Long insurancePolicyId, String productId, String offerId, String category, Date createdAt,
            Date updatedAt, BigDecimal totalMonthlyPremiumAmount, BigDecimal totalCoverageAmount,
            Map<String, BigDecimal> coverages, List<Assistance> assistances, Customer customer) {
        this.id = id;
        this.insurancePolicyId = insurancePolicyId;
        this.productId = productId;
        this.offerId = offerId;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalMonthlyPremiumAmount = totalMonthlyPremiumAmount;
        this.totalCoverageAmount = totalCoverageAmount;
        this.coverages = coverages;
        this.assistances = assistances;
        this.customer = customer;
    }

    public static Quote create(
        Long id, 
        Long insurancePolicyId, 
        String productId, 
        String offerId, 
        String category, 
        Date createdAt,
        Date updatedAt, 
        BigDecimal totalMonthlyPremiumAmount, 
        BigDecimal totalCoverageAmount,
        Map<String, BigDecimal> coverages, 
        List<Assistance> assistances, 
        Customer customer) {

        return new Quote(
            id, 
            insurancePolicyId, 
            productId, 
            offerId, 
            category, 
            createdAt,
            updatedAt, 
            totalMonthlyPremiumAmount, 
            totalCoverageAmount,
            coverages, 
            assistances, 
            customer);
    }

    public static Quote create(
        String productId, 
        String offerId, 
        String category, 
        BigDecimal totalMonthlyPremiumAmount, 
        BigDecimal totalCoverageAmount,
        Map<String, BigDecimal> coverages, 
        List<Assistance> assistances, 
        Customer customer) {

        return create(
            null, 
            null, 
            productId, 
            offerId, 
            category, 
            null,
            null, 
            totalMonthlyPremiumAmount, 
            totalCoverageAmount,
            coverages, 
            assistances, 
            customer);
    }

    public Long getId() {
        return id;
    }

    public Long getInsurancePolicyId() {
        return insurancePolicyId;
    }

    public String getProductId() {
        return productId;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getCategory() {
        return category;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public BigDecimal getTotalMonthlyPremiumAmount() {
        return totalMonthlyPremiumAmount;
    }

    public BigDecimal getTotalCoverageAmount() {
        return totalCoverageAmount;
    }

    public Map<String, BigDecimal> getCoverages() {
        return coverages;
    }

    public List<Assistance> getAssistances() {
        return assistances;
    }

    public Customer getCostumer() {
        return customer;
    }

    public Boolean isValidate() {
        return _isValidated;
    }

    public List<InvalidationReason> validate(ProductGetterInterface productGetter, OfferGetterInterface offerGetter) throws SystemError {
        invalidationReasons = new ArrayList<>();

        if (productId == null) {
            invalidationReasons.add(InvalidationReason.PRODUCT_ID_FIELD_REQUIRED);
        } else {
            if (offerId == null) {
                invalidationReasons.add(InvalidationReason.OFFER_ID_FIELD_REQUIRED);
            } else {
                Response<Product> response = productGetter.getProduct(productId);
                if (response.errors() != null && response.errors().size() > 0) {
                    throw new SystemError(response.errors());
                }
                Product product = response.getData();
                if (product == null) {
                    invalidationReasons.add(InvalidationReason.PRODUCT_NOT_FOUND);
                } else if (!product.active()) {
                    invalidationReasons.add(InvalidationReason.INACTIVE_PRODUCT);
                } else {
                    List<String> offerIds = product.offers();
                    if (offerIds.indexOf(this.offerId) < 0) {
                        invalidationReasons.add(InvalidationReason.INVALID_OFFER);
                    } else {                
                        validateOffer(offerGetter);
                    }
                }
            }
        }
        return invalidationReasons;
    }

    private void validateOffer(OfferGetterInterface getter) throws SystemError {

        Response<Offer> response = getter.getOffer(offerId);

        if (response.errors() != null) {
            throw new SystemError(response.errors());
        }
        Offer offer = response.getData();
        if (offer == null) {
            invalidationReasons.add(InvalidationReason.OFFER_NOT_FOUND);
        } else {
            if (!offer.active()) {
                invalidationReasons.add(InvalidationReason.INACTIVE_OFFER);
            }
            validateCoverages(offer);
            validateMonthlyPremium(offer);
            validateAssistances(offer);
        }

    }

    private void validateCoverages(Offer offer) {
        if (totalCoverageAmount == null || totalCoverageAmount.compareTo(BigDecimal.ZERO) == 0) {
            invalidationReasons.add(InvalidationReason.TOTAL_COVERAGE_REQUIRED);
            return;
        }

        Map<String, BigDecimal> offerCoverages = offer.coverages();
        if (coverages == null || coverages.size() == 0) {
            invalidationReasons.add(InvalidationReason.COVERAGES_IS_EMPTY_OR_NULL);
            return;
        }

        Set<String> validCoverages = offerCoverages.keySet();

        BigDecimal total = BigDecimal.ZERO;
        for (String coverage : coverages.keySet()) {
            
            if (validCoverages.contains(coverage)) {
                BigDecimal amount = coverages.get(coverage);
                if (coverages.get(coverage).compareTo(offerCoverages.get(coverage)) > 0) {
                    invalidationReasons.add(InvalidationReason.COVERAGE_EXCEEDED_AMOUNT);
                    return;
                }
                total = total.add(amount);
            } else {
                invalidationReasons.add(InvalidationReason.INVALID_COVERAGE_TYPE);
                return;
            }
            
        }
        if (totalCoverageAmount.compareTo(total) > 0) {
            invalidationReasons.add(InvalidationReason.COVERAGE_EXCEEDED_AMOUNT);
        }

    }

    private void validateMonthlyPremium(Offer offer) {
        if (totalMonthlyPremiumAmount == null || totalMonthlyPremiumAmount.compareTo(BigDecimal.ZERO) == 0) {
            invalidationReasons.add(InvalidationReason.TOTAL_MONTHLY_PREMUIM_REQUIRED);
        } else {
            if (totalMonthlyPremiumAmount.compareTo(offer.monthlyPremiumAmount().maxAmount()) > 0) {
                invalidationReasons.add(InvalidationReason.TOTAL_MONTHLY_PREMIUM_EXCEEDS_MAX);
            } else if (totalMonthlyPremiumAmount.compareTo(offer.monthlyPremiumAmount().minAmount()) < 0){
                invalidationReasons.add(InvalidationReason.TOTAL_MONTHLY_PREMIUM_BELOW_MIN);
            }
        }
    }

    private void validateAssistances(Offer offer) {
        if (assistances != null) {
            if (offer.assistances() == null || offer.assistances().size() == 0) {
                if (assistances == null || assistances.size() == 0) {
                    return;
                } else {
                    invalidationReasons.add(InvalidationReason.INVALID_ASSISTANCE);
                }
            }
            else {
                for (Assistance assistance : assistances) {
                    if (!offer.assistances().contains(assistance)) {
                        invalidationReasons.add(InvalidationReason.INVALID_ASSISTANCE);
                        break;
                    }
                }
            }
        }
    }

}
