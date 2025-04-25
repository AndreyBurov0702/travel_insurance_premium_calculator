package org.javaguru.travel.insurance.core.validations.agreement;

import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.javaguru.travel.insurance.dto.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmptyMedicalRiskLimitLevelValidationTest {

    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private ValidationError expectedError ;
    private TravelCalculatePremiumRequestV1 request;

    @BeforeEach
    void setUp() {
        request = new TravelCalculatePremiumRequestV1();
    }
    @Test
    void shouldReturnValidationErrorWhenMedicalRiskLimitLevelEnabledAndNullOrBlank() {
        request.setSelectedRisks(List.of("TRAVEL_MEDICAL"));
        request.setMedicalRiskLimitLevel(null);
        when(errorFactory.buildError("ERROR_CODE_13")).thenReturn(expectedError);
        var validation = new EmptyMedicalRiskLimitLevelValidation(true, errorFactory);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isPresent());
        assertEquals(expectedError, errorOptional.get());
    }
    @Test
    void shouldNotReturnValidationErrorWhenMedicalRiskLimitLevelEnabledAndIsNotBlank() {
        request.setSelectedRisks(List.of("TRAVEL_MEDICAL"));
        request.setMedicalRiskLimitLevel("LEVEL_10000");
        var validation = new EmptyMedicalRiskLimitLevelValidation(true, errorFactory);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isEmpty());
    }
    @Test
    void shouldNotReturnValidationErrorWhenMedicalRiskLimitLevelNotEnabledAndIsBlank() {
        request.setSelectedRisks(List.of("TRAVEL_MEDICAL"));
        request.setMedicalRiskLimitLevel("");
        var validation = new EmptyMedicalRiskLimitLevelValidation(false, errorFactory);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isEmpty());}
}
