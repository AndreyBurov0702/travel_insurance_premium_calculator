package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmptyTMMedicalRiskLimitLevelValidationTest {

    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private ValidationErrorDTO expectedError ;
    private AgreementDTO agreement;
    private PersonDTO person;

    @BeforeEach
    void setUp() {
        agreement = new AgreementDTO();
        person = new PersonDTO();
    }
    @Test
    void shouldReturnValidationErrorWhenMedicalRiskLimitLevelEnabledAndNullOrBlank() {
        agreement.setSelectedRisks(List.of("TRAVEL_MEDICAL"));
        person.setMedicalRiskLimitLevel(null);
        when(errorFactory.buildError("ERROR_CODE_13")).thenReturn(expectedError);
        var validation = new EmptyMedicalRiskLimitLevelValidation(true, errorFactory);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isPresent());
        assertEquals(expectedError, errorOptional.get());
    }
    @Test
    void shouldNotReturnValidationErrorWhenMedicalRiskLimitLevelEnabledAndIsNotBlank() {
        agreement.setSelectedRisks(List.of("TRAVEL_MEDICAL"));
        person.setMedicalRiskLimitLevel("LEVEL_10000");
        var validation = new EmptyMedicalRiskLimitLevelValidation(true, errorFactory);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
    }
    @Test
    void shouldNotReturnValidationErrorWhenMedicalRiskLimitLevelNotEnabledAndIsBlank() {
        agreement.setSelectedRisks(List.of("TRAVEL_MEDICAL"));
        person.setMedicalRiskLimitLevel("");
        var validation = new EmptyMedicalRiskLimitLevelValidation(false, errorFactory);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());}
}
