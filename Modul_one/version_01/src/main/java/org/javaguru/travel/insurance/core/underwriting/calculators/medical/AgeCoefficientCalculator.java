package org.javaguru.travel.insurance.core.underwriting.calculators.medical;


import org.javaguru.travel.insurance.core.domain.AgeCoefficient;
import org.javaguru.travel.insurance.core.repositories.AgeCoefficientRepository;
import org.javaguru.travel.insurance.core.util.DateTimeUtil;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Component
class AgeCoefficientCalculator {
    private final Boolean medicalRiskAgeCoefficientEnabled;
    private final DateTimeUtil dateTimeUtil;
    private final AgeCoefficientRepository ageCoefficientRepository;

    AgeCoefficientCalculator(@Value("${medical.risk.age.coefficient.enabled:false}")
                             Boolean medicalRiskAgeCoefficientEnabled,
                             DateTimeUtil dateTimeUtil,
                             AgeCoefficientRepository ageCoefficientRepository) {
        this.medicalRiskAgeCoefficientEnabled = medicalRiskAgeCoefficientEnabled;
        this.dateTimeUtil = dateTimeUtil;
        this.ageCoefficientRepository = ageCoefficientRepository;
    }

    BigDecimal calculate(TravelCalculatePremiumRequestV1 request) {
        return medicalRiskAgeCoefficientEnabled
                ? getCoefficient(request)
                : getDefaultValue();
    }

    private BigDecimal getCoefficient(TravelCalculatePremiumRequestV1 request) {
        int age = calculateAge(request);
        return ageCoefficientRepository.findCoefficient(age)
                .map(AgeCoefficient::getCoefficient)
                .orElseThrow(() -> new RuntimeException("Age coefficient not found for age = " + age));
    }

    private Integer calculateAge(TravelCalculatePremiumRequestV1 request) {
        LocalDate personBirthDate = toLocalDate(request.getPersonBirthDate());
        LocalDate currentDate = toLocalDate(dateTimeUtil.getCurrentDateTime());
        return Period.between(personBirthDate, currentDate).getYears();
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private static BigDecimal getDefaultValue() {
        return BigDecimal.ONE;
    }
}
