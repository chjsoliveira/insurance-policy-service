package com.acme.insurance.domain.service;

import com.acme.insurance.domain.model.Category;
import com.acme.insurance.domain.model.PolicyRequest;
import com.acme.insurance.domain.model.RiskClassification;

import java.math.BigDecimal;

public class RiskValidationService {
    public static boolean validate(RiskClassification classification, PolicyRequest request) {
        BigDecimal value = request.getInsuredAmount();
        Category category = request.getCategory();

        switch (classification) {
            case REGULAR:
                if (category == Category.VIDA || category == Category.RESIDENCIAL) {
                    return value.compareTo(new BigDecimal("500000")) <= 0;
                } else if (category == Category.AUTO) {
                    return value.compareTo(new BigDecimal("350000")) <= 0;
                } else {
                    return value.compareTo(new BigDecimal("255000")) <= 0;
                }

            case HIGH_RISK:
                if (category == Category.AUTO) {
                    return value.compareTo(new BigDecimal("250000")) <= 0;
                } else if (category == Category.RESIDENCIAL) {
                    return value.compareTo(new BigDecimal("150000")) <= 0;
                } else {
                    return value.compareTo(new BigDecimal("125000")) <= 0;
                }

            case PREFERENTIAL:
                if (category == Category.VIDA) {
                    return value.compareTo(new BigDecimal("800000")) <= 0;
                } else if (category == Category.AUTO || category == Category.RESIDENCIAL) {
                    return value.compareTo(new BigDecimal("450000")) <= 0;
                } else {
                    return value.compareTo(new BigDecimal("375000")) <= 0;
                }

            case NO_INFORMATION:
                if (category == Category.VIDA || category == Category.RESIDENCIAL) {
                    return value.compareTo(new BigDecimal("200000")) <= 0;
                } else if (category == Category.AUTO) {
                    return value.compareTo(new BigDecimal("75000")) <= 0;
                } else {
                    return value.compareTo(new BigDecimal("55000")) <= 0;
                }

            default:
                return false;
        }
    }
}
