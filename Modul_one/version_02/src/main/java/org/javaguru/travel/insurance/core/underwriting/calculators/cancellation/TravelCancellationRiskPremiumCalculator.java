package org.javaguru.travel.insurance.core.underwriting.calculators.cancellation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.underwriting.TravelRiskPremiumCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TravelCancellationRiskPremiumCalculator implements TravelRiskPremiumCalculator {

    private final TCTravelCostCoefficientCalculator travelCostCoefficientCalculator;
    private final TCAgeCoefficientCalculator ageCoefficientCalculator;
    private final TCCountrySafetyRatingCoefficientCalculator countrySafetyRatingCoefficientCalculator;

    @Override
    public BigDecimal calculatePremium(AgreementDTO agreement, PersonDTO person) {
        var travelCostCoefficient = travelCostCoefficientCalculator.calculate(person);
        var ageCoefficient = ageCoefficientCalculator.calculate(person);
        var countrySafetyRatingCoefficient = countrySafetyRatingCoefficientCalculator.calculate(agreement);
        return travelCostCoefficient
                .add(ageCoefficient)
                .add(countrySafetyRatingCoefficient)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getRiskIc() {
        return "TRAVEL_CANCELLATION";
    }
}
