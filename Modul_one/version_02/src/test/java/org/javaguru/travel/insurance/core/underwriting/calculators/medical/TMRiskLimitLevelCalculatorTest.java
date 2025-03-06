package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.domain.TMMedicalRiskLimitLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TMRiskLimitLevelCalculatorTest {
    @Mock
    private PersonDTO personDTO;
    @Mock
    private TMMedicalRiskLimitLevelRepository medicalRiskLimitLevelRepository;
    @Mock
    private TMMedicalRiskLimitLevel medicalRiskLimitLevel;
    @InjectMocks
    private TMRiskLimitLevelCalculator riskLimitLevelCalculator;

    @Test
    void shouldCalculateCoefficientWhenEnabledTrue() {
        when(personDTO.getMedicalRiskLimitLevel()).thenReturn("LEVEL_10000");
        when(medicalRiskLimitLevelRepository.findByMedicalRiskLimitLevelIc("LEVEL_10000"))
                .thenReturn(Optional.of(medicalRiskLimitLevel));
        when(medicalRiskLimitLevel.getCoefficient()).thenReturn(new BigDecimal("1.00"));
        ReflectionTestUtils.setField(riskLimitLevelCalculator, "medicalRiskLimitLevelEnabled", true);
        BigDecimal result = riskLimitLevelCalculator.calculate(personDTO);
        assertEquals(new BigDecimal("1.00"), result);
    }

    @Test
    void shouldThrowExceptionWhenMedicalRiskLimitLevelIcNotExist() {
        when(personDTO.getMedicalRiskLimitLevel()).thenReturn("LEVEL_99999");
        when(medicalRiskLimitLevelRepository.findByMedicalRiskLimitLevelIc("LEVEL_99999"))
                .thenReturn(Optional.empty());
        ReflectionTestUtils.setField(riskLimitLevelCalculator, "medicalRiskLimitLevelEnabled", true);
        assertThrows(RuntimeException.class, () -> riskLimitLevelCalculator.calculate(personDTO));
    }
    @Test
    void shouldReturnDefaultValueWhenEnabledFalse() {
        ReflectionTestUtils.setField(riskLimitLevelCalculator, "medicalRiskLimitLevelEnabled", false);
        BigDecimal result = riskLimitLevelCalculator.calculate(personDTO);
        assertEquals(BigDecimal.ONE, result);
    }
}

