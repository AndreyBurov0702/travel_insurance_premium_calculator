package org.javaguru.travel.insurance.core.underwriting.calculators.cancellation;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.domain.TCCountrySafetyRatingCoefficient;
import org.javaguru.travel.insurance.core.repositories.TCCountrySafetyRatingCoefficientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TCCountrySafetyRatingCoefficientCalculatorTest {
    @Mock
    private TCCountrySafetyRatingCoefficientRepository countrySafetyRatingCoefficientRepository;
    @Mock
    private TCCountrySafetyRatingCoefficient countrySafetyRatingCoefficient;

    @InjectMocks
    private TCCountrySafetyRatingCoefficientCalculator calculator;
    private AgreementDTO agreement;

    @BeforeEach
    void setUp() {
        agreement = new AgreementDTO();
        agreement.setCountry("US");
    }
    @Test
    void shouldCalculateDayRateWhenCountryDayRateExists() {
        BigDecimal expectedDayRate = BigDecimal.valueOf(10.0);
        when(countrySafetyRatingCoefficient.getCoefficient()).thenReturn(expectedDayRate);
        when(countrySafetyRatingCoefficientRepository.findByCountryIc(agreement.getCountry()))
                .thenReturn(Optional.of(countrySafetyRatingCoefficient));
        BigDecimal result = calculator.calculate(agreement);
        assertEquals(expectedDayRate, result);
    }
    @Test
    void shouldThrowException() {
        when(countrySafetyRatingCoefficientRepository.findByCountryIc(agreement.getCountry())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> calculator.calculate(agreement));
        assertEquals("Country safety rating coefficient not found by country id = " + agreement.getCountry(), exception.getMessage());
    }
}
