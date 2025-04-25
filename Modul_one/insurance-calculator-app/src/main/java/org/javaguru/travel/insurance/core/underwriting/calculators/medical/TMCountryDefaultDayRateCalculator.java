package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.domain.TMCountryDefaultDayRate;
import org.javaguru.travel.insurance.core.repositories.TMCountryDefaultDayRateRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TMCountryDefaultDayRateCalculator {

    private final TMCountryDefaultDayRateRepository countryDefaultDayRateRepository;

    BigDecimal calculate(AgreementDTO agreement){
        return countryDefaultDayRateRepository.findByCountryIc(agreement.getCountry())
                .map(TMCountryDefaultDayRate::getDefaultDayRate)
                .orElseThrow(() -> new RuntimeException("Country day rate not found by country id = " + agreement.getCountry()));
    }
}
