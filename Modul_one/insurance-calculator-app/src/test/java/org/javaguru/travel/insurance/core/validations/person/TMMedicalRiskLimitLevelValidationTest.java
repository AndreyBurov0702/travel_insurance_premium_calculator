package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.domain.ClassifierValue;
import org.javaguru.travel.insurance.core.repositories.ClassifierValueRepository;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TMMedicalRiskLimitLevelValidationTest {
    @Mock
    private ClassifierValueRepository classifierValueRepository;
    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private AgreementDTO agreement;
    @Mock
    private PersonDTO person;

    @InjectMocks
    private MedicalRiskLimitLevelValidation validation;

    @Test
    public void shouldNotReturnErrorWhenMedicalRiskLimitLevelIsNull() {
        when(person.getMedicalRiskLimitLevel()).thenReturn(null);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(classifierValueRepository, errorFactory);
    }

    @Test
    public void shouldNotReturnErrorWhenMedicalRiskLimitLevelIsBlank() {
        when(person.getMedicalRiskLimitLevel()).thenReturn("");
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(classifierValueRepository, errorFactory);
    }
    @Test
    public void shouldNotReturnErrorWhenMedicalRiskLimitLevelExistInDb() {
        when(person.getMedicalRiskLimitLevel()).thenReturn("LEVEL_10000");
        ClassifierValue classifierValue = mock(ClassifierValue.class);
        when(classifierValueRepository.findByClassifierTitleAndIc("MEDICAL_RISK_LIMIT_LEVEL", "LEVEL_10000"))
                .thenReturn(Optional.of(classifierValue));
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }
    @Test
    public void shouldReturnError() {
        when(person.getMedicalRiskLimitLevel()).thenReturn("LEVEL_10000");
        when(classifierValueRepository.findByClassifierTitleAndIc("MEDICAL_RISK_LIMIT_LEVEL", "LEVEL_10000"))
                .thenReturn(Optional.empty());
        ValidationErrorDTO validationError = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError(eq("ERROR_CODE_14"), any())).thenReturn(validationError);
        Optional<ValidationErrorDTO> validationErrorOpt = validation.validate(agreement, person);
        assertTrue(validationErrorOpt.isPresent());
        assertSame(validationError, validationErrorOpt.get());
    }
}
