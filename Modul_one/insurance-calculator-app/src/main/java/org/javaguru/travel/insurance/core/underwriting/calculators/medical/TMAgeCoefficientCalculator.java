package org.javaguru.travel.insurance.core.underwriting.calculators.medical;


import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.domain.TMAgeCoefficient;
import org.javaguru.travel.insurance.core.repositories.TMAgeCoefficientRepository;
import org.javaguru.travel.insurance.core.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Component
class TMAgeCoefficientCalculator {
    private final Boolean medicalRiskAgeCoefficientEnabled;
    private final DateTimeUtil dateTimeUtil;
    private final TMAgeCoefficientRepository ageCoefficientRepository;

    TMAgeCoefficientCalculator(@Value("${medical.risk.age.coefficient.enabled:false}")
                             Boolean medicalRiskAgeCoefficientEnabled,
                               DateTimeUtil dateTimeUtil,
                               TMAgeCoefficientRepository ageCoefficientRepository) {
        this.medicalRiskAgeCoefficientEnabled = medicalRiskAgeCoefficientEnabled;
        this.dateTimeUtil = dateTimeUtil;
        this.ageCoefficientRepository = ageCoefficientRepository;
    }

    BigDecimal calculate(PersonDTO person) {
        return medicalRiskAgeCoefficientEnabled
                ? getCoefficient(person)
                : getDefaultValue();
    }

    private BigDecimal getCoefficient(PersonDTO person) {
        int age = calculateAge(person);
        return ageCoefficientRepository.findCoefficient(age)
                .map(TMAgeCoefficient::getCoefficient)
                .orElseThrow(() -> new RuntimeException("Age coefficient not found for age = " + age));
    }

    private Integer calculateAge(PersonDTO person) {
        LocalDate personBirthDate = toLocalDate(person.getPersonBirthDate());
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
