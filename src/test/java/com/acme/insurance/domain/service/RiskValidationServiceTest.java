
package com.acme.insurance.domain.service;

import com.acme.insurance.domain.model.Category;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.domain.model.RiskClassification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RiskValidationServiceTest {

    private RiskValidationService service;


    @Test
    void shouldApproveRegularClientForAutoWhenWithinLimit() {
        PolicyRequest request = buildRequest(Category.AUTO, new BigDecimal("300000"));
        boolean result = RiskValidationService.validate(RiskClassification.REGULAR, request);
        assertTrue(result);
    }

    @Test
    void shouldRejectRegularClientForAutoWhenAboveLimit() {
        PolicyRequest request = buildRequest(Category.AUTO, new BigDecimal("400000"));
        boolean result = RiskValidationService.validate(RiskClassification.REGULAR, request);
        assertFalse(result);
    }

    @Test
    void shouldApproveRegularClientForVidaWhenWithinLimit() {
        PolicyRequest request = buildRequest(Category.VIDA, new BigDecimal("500000"));
        boolean result = RiskValidationService.validate(RiskClassification.REGULAR, request);
        assertTrue(result);
    }

    @Test
    void shouldApproveRegularClientForResidencialWhenWithinLimit() {
        PolicyRequest request = buildRequest(Category.RESIDENCIAL, new BigDecimal("500000"));
        boolean result = RiskValidationService.validate(RiskClassification.REGULAR, request);
        assertTrue(result);
    }

    @Test
    void shouldApproveRegularClientForEmpresarialWhenWithinLimit() {
        PolicyRequest request = buildRequest(Category.EMPRESARIAL, new BigDecimal("255000"));
        boolean result = RiskValidationService.validate(RiskClassification.REGULAR, request);
        assertTrue(result);
    }

    @Test
    void shouldApprovePreferentialClientForVidaWhenWithinLimit() {
        PolicyRequest request = buildRequest(Category.VIDA, new BigDecimal("750000"));
        boolean result = RiskValidationService.validate(RiskClassification.PREFERENTIAL, request);
        assertTrue(result);
    }

    @Test
    void shouldApproveHighRiskClientForAutoWhenAboveLimit() {
        PolicyRequest request = buildRequest(Category.AUTO, new BigDecimal("250000"));
        boolean result = RiskValidationService.validate(RiskClassification.HIGH_RISK, request);
        assertTrue(result);
    }

    @Test
    void shouldApproveHighRiskClientForResidencialWhenAboveLimit() {
        PolicyRequest request = buildRequest(Category.RESIDENCIAL, new BigDecimal("150000"));
        boolean result = RiskValidationService.validate(RiskClassification.HIGH_RISK, request);
        assertTrue(result);
    }

    @Test
    void shouldRejectHighRiskClientForOtherWhenAboveLimit() {
        PolicyRequest request = buildRequest(Category.EMPRESARIAL, new BigDecimal("200000"));
        boolean result = RiskValidationService.validate(RiskClassification.HIGH_RISK, request);
        assertFalse(result);
    }

    @Test
    void shouldRejectPreferentialClientForVidaAboveLimit() {
        PolicyRequest request = new PolicyRequest();
        request.setCategory(Category.VIDA);
        request.setInsuredAmount(new BigDecimal("900000"));

        assertFalse(RiskValidationService.validate(RiskClassification.PREFERENTIAL, request));
    }
    @Test
    void shouldApprovePreferentialClientForAutoAboveLimit() {
        PolicyRequest request = new PolicyRequest();
        request.setCategory(Category.AUTO);
        request.setInsuredAmount(new BigDecimal("450000"));

        assertTrue(RiskValidationService.validate(RiskClassification.PREFERENTIAL, request));
    }
    @Test
    void shouldApprovePreferentialClientForResidencialAboveLimit() {
        PolicyRequest request = new PolicyRequest();
        request.setCategory(Category.RESIDENCIAL);
        request.setInsuredAmount(new BigDecimal("450000"));

        assertTrue(RiskValidationService.validate(RiskClassification.PREFERENTIAL, request));
    }
    @Test
    void shouldApprovePreferentialClientForEmpresarialAboveLimit() {
        PolicyRequest request = new PolicyRequest();
        request.setCategory(Category.EMPRESARIAL);
        request.setInsuredAmount(new BigDecimal("375000"));

        assertTrue(RiskValidationService.validate(RiskClassification.PREFERENTIAL, request));
    }
    @Test
    void shouldApproveNoInfoClientForResidencialUnderLimit() {
        PolicyRequest request = new PolicyRequest();
        request.setCategory(Category.RESIDENCIAL);
        request.setInsuredAmount(new BigDecimal("150000"));

        assertTrue(RiskValidationService.validate(RiskClassification.NO_INFORMATION, request));
    }

    @Test
    void shouldRejectNoInfoClientForAutoAboveLimit() {
        PolicyRequest request = new PolicyRequest();
        request.setCategory(Category.AUTO);
        request.setInsuredAmount(new BigDecimal("80000"));

        assertFalse(RiskValidationService.validate(RiskClassification.NO_INFORMATION, request));
    }

    @Test
    void shouldApproveNoInfoClientForEmpresarialUnderLimit() {
        PolicyRequest request = new PolicyRequest();
        request.setCategory(Category.EMPRESARIAL);
        request.setInsuredAmount(new BigDecimal("55000"));

        assertTrue(RiskValidationService.validate(RiskClassification.NO_INFORMATION, request));
    }

    private PolicyRequest buildRequest(Category category, BigDecimal insuredAmount) {
        PolicyRequest request = new PolicyRequest();
        request.setRequestId(UUID.randomUUID());
        request.setCategory(category);
        request.setInsuredAmount(insuredAmount);
        return request;
    }
}
