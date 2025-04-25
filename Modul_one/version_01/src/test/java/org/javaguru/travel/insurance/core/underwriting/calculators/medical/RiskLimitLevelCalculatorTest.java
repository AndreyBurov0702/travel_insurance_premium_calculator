package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import org.javaguru.travel.insurance.core.domain.MedicalRiskLimitLevel;
import org.javaguru.travel.insurance.core.repositories.MedicalRiskLimitLevelRepository;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiskLimitLevelCalculatorTest {
    @Mock
    private TravelCalculatePremiumRequestV1 request;
    @Mock
    private MedicalRiskLimitLevelRepository medicalRiskLimitLevelRepository;
    @Mock
    private MedicalRiskLimitLevel medicalRiskLimitLevel;
    @InjectMocks
    private RiskLimitLevelCalculator riskLimitLevelCalculator;

    @Test
    void shouldCalculateCoefficientWhenEnabledTrue() {
        when(request.getMedicalRiskLimitLevel()).thenReturn("LEVEL_10000");
        when(medicalRiskLimitLevelRepository.findByMedicalRiskLimitLevelIc("LEVEL_10000"))
                .thenReturn(Optional.of(medicalRiskLimitLevel));
        when(medicalRiskLimitLevel.getCoefficient()).thenReturn(new BigDecimal("1.00"));
        ReflectionTestUtils.setField(riskLimitLevelCalculator, "medicalRiskLimitLevelEnabled", true);
        BigDecimal result = riskLimitLevelCalculator.calculate(request);
        assertEquals(new BigDecimal("1.00"), result);
    }

    @Test
    void shouldThrowExceptionWhenMedicalRiskLimitLevelIcNotExist() {
        when(request.getMedicalRiskLimitLevel()).thenReturn("LEVEL_99999");
        when(medicalRiskLimitLevelRepository.findByMedicalRiskLimitLevelIc("LEVEL_99999"))
                .thenReturn(Optional.empty());
        ReflectionTestUtils.setField(riskLimitLevelCalculator, "medicalRiskLimitLevelEnabled", true);
        assertThrows(RuntimeException.class, () -> riskLimitLevelCalculator.calculate(request));
    }
    @Test
    void shouldReturnDefaultValueWhenEnabledFalse() {
        ReflectionTestUtils.setField(riskLimitLevelCalculator, "medicalRiskLimitLevelEnabled", false);
        BigDecimal result = riskLimitLevelCalculator.calculate(request);
        assertEquals(BigDecimal.ONE, result);
    }
}

