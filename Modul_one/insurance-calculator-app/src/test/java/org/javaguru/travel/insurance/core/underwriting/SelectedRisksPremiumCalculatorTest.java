package org.javaguru.travel.insurance.core.underwriting;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectedRisksPremiumCalculatorTest {
    @Mock
    private TravelRiskPremiumCalculator riskPremiumCalculator1;
    @Mock
    private TravelRiskPremiumCalculator riskPremiumCalculator2;
    @Mock
    private AgreementDTO agreement;
    @Mock
    private PersonDTO person;

    private SelectedRisksPremiumCalculator calculator;

    @BeforeEach
    void init(){
        var riskPremiumCalculators = List.of(riskPremiumCalculator1, riskPremiumCalculator2);
        calculator = new SelectedRisksPremiumCalculator(riskPremiumCalculators);
    }
    @Test
    void shouldCalculatePremiumForOneRisk() {
        when(riskPremiumCalculator1.getRiskIc()).thenReturn("TRAVEL_MEDICAL");
        when(riskPremiumCalculator1.calculatePremium(any(), any())).thenReturn(BigDecimal.ONE);
        when(agreement.getSelectedRisks()).thenReturn(List.of("TRAVEL_MEDICAL"));
        List<RiskDTO> riskPremiums = calculator.calculatePremiumForAllRisks(agreement, person);
        assertEquals(riskPremiums.size(), 1);
        assertEquals(riskPremiums.get(0).getRiskIc(), "TRAVEL_MEDICAL");
        assertEquals(riskPremiums.get(0).getPremium(), BigDecimal.ONE);
    }
    @Test
    void shouldCalculatePremiumForTwoRisks() {
        when(riskPremiumCalculator1.getRiskIc()).thenReturn("TRAVEL_MEDICAL");
        when(riskPremiumCalculator2.getRiskIc()).thenReturn("TRAVEL_EVACUATION");
        when(riskPremiumCalculator1.calculatePremium(any(), any())).thenReturn(BigDecimal.ONE);
        when(riskPremiumCalculator2.calculatePremium(any(), any())).thenReturn(BigDecimal.ONE);
        when(agreement.getSelectedRisks()).thenReturn(List.of("TRAVEL_MEDICAL", "TRAVEL_EVACUATION"));
        List<RiskDTO> riskPremiums = calculator.calculatePremiumForAllRisks(agreement, person);
        assertEquals(riskPremiums.size(), 2);
        assertEquals(riskPremiums.get(0).getRiskIc(), "TRAVEL_MEDICAL");
        assertEquals(riskPremiums.get(0).getPremium(), BigDecimal.ONE);
        assertEquals(riskPremiums.get(1).getRiskIc(), "TRAVEL_EVACUATION");
        assertEquals(riskPremiums.get(1).getPremium(), BigDecimal.ONE);
    }
    @Test
    void shouldThrowExceptionWhenSelectedRiskTypeNotSupported() {
        when(riskPremiumCalculator1.getRiskIc()).thenReturn("TRAVEL_MEDICAL");
        when(riskPremiumCalculator2.getRiskIc()).thenReturn("TRAVEL_EVACUATION");
        when(agreement.getSelectedRisks()).thenReturn(List.of("NOT_SUPPORTED_RISK_TYPE"));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> calculator.calculatePremiumForAllRisks(agreement, person));
        assertEquals(exception.getMessage(), "Not supported riskIc = NOT_SUPPORTED_RISK_TYPE");
    }
}

