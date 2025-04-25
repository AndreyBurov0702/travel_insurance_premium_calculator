package org.javaguru.travel.insurance.core.validations.agreement;

import org.javaguru.travel.insurance.core.domain.ClassifierValue;
import org.javaguru.travel.insurance.core.repositories.ClassifierValueRepository;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.javaguru.travel.insurance.dto.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalRiskLimitLevelValidationTest {
    @Mock
    private ClassifierValueRepository classifierValueRepository;
    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private TravelCalculatePremiumRequestV1 request;

    @InjectMocks
    private MedicalRiskLimitLevelValidation validation;

    @Test
    public void shouldNotReturnErrorWhenMedicalRiskLimitLevelIsNull() {
        when(request.getMedicalRiskLimitLevel()).thenReturn(null);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(classifierValueRepository, errorFactory);
    }

    @Test
    public void shouldNotReturnErrorWhenMedicalRiskLimitLevelIsBlank() {
        when(request.getMedicalRiskLimitLevel()).thenReturn("");
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(classifierValueRepository, errorFactory);
    }
    @Test
    public void shouldNotReturnErrorWhenMedicalRiskLimitLevelExistInDb() {
        when(request.getMedicalRiskLimitLevel()).thenReturn("LEVEL_10000");
        ClassifierValue classifierValue = mock(ClassifierValue.class);
        when(classifierValueRepository.findByClassifierTitleAndIc("MEDICAL_RISK_LIMIT_LEVEL", "LEVEL_10000"))
                .thenReturn(Optional.of(classifierValue));
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }
    @Test
    public void shouldReturnError() {
        when(request.getMedicalRiskLimitLevel()).thenReturn("LEVEL_10000");
        when(classifierValueRepository.findByClassifierTitleAndIc("MEDICAL_RISK_LIMIT_LEVEL", "LEVEL_10000"))
                .thenReturn(Optional.empty());
        ValidationError validationError = mock(ValidationError.class);
        when(errorFactory.buildError("ERROR_CODE_14")).thenReturn(validationError);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isPresent());
        assertSame(validationError, errorOptional.get());
    }
}
