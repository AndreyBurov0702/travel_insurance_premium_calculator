package org.javaguru.travel.insurance.core.underwriting;


import org.javaguru.travel.insurance.dto.RiskPremium;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelPremiumUnderwritingImplTest {
    @Mock
    private TravelCalculatePremiumRequestV1 request;
    @Mock
    private SelectedRisksPremiumCalculator selectedRisksPremiumCalculator;
    @InjectMocks
    private TravelPremiumUnderwritingImpl premiumUnderwriting;

    @Test
    void shouldCalculateTotalPremiumAsSumOfRiskPremiums() {
        List<RiskPremium> riskPremiums = List.of(
                new RiskPremium("TRAVEL_MEDICAL", BigDecimal.ONE),
                new RiskPremium("TRAVEL_EVACUATION", BigDecimal.ONE)
        );
        when(selectedRisksPremiumCalculator.calculatePremiumForAllRisks(request)).thenReturn(riskPremiums);
        TravelPremiumCalculationResult premiumCalculationResult = premiumUnderwriting.calculatePremium(request);
        assertEquals(premiumCalculationResult.totalPremium(), new BigDecimal(2));
    }
}

