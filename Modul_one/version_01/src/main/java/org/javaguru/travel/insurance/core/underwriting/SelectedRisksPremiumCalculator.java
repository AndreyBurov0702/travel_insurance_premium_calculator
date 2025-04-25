package org.javaguru.travel.insurance.core.underwriting;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.dto.RiskPremium;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SelectedRisksPremiumCalculator {

    private final List<TravelRiskPremiumCalculator> riskPremiumCalculators;

    List <RiskPremium> calculatePremiumForAllRisks (TravelCalculatePremiumRequestV1 request){
        return request.getSelectedRisks().stream()
                .map(rickIc-> new RiskPremium(rickIc, calculatePremiumForRisk(rickIc, request)))
                .toList();
    }
    private BigDecimal calculatePremiumForRisk(String riskIc, TravelCalculatePremiumRequestV1 request){
        return findRiskPremiumCalculator(riskIc).calculatePremium(request);
    }
    private TravelRiskPremiumCalculator findRiskPremiumCalculator(String riskIc){
        return riskPremiumCalculators.stream()
                .filter(riskCalculator -> riskCalculator.getRiskIc().equals(riskIc))
                .findFirst()
                .orElseThrow(()->new RuntimeException("Not supported riskIc = " + riskIc));
    }
}
