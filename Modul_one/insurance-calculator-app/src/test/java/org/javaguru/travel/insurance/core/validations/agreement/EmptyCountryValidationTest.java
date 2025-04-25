package org.javaguru.travel.insurance.core.validations.agreement;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmptyCountryValidationTest {

    @Mock
    private AgreementDTO agreement;
    @Mock
    private ValidationErrorFactory errorFactory;
    @InjectMocks
    private EmptyCountryValidation validation;

    @Test
    public void shouldReturnNoErrorWhenCountryIsPresent() {
        when(agreement.getCountry()).thenReturn("SPAIN");
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement);
        assertTrue(errorOptional.isEmpty());
    }

    @Test
    public void shouldReturnErrorWhenCountryIsNull() {
        when(agreement.getCountry()).thenReturn(null);
        when(errorFactory.buildError("ERROR_CODE_10"))
                .thenReturn(new ValidationErrorDTO("ERROR_CODE_10", "Country must be provided when TRAVEL_MEDICAL is selected"));
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement);
        assertTrue(errorOptional.isPresent());
        assertEquals("ERROR_CODE_10", errorOptional.get().getErrorCode());
        assertEquals("Country must be provided when TRAVEL_MEDICAL is selected", errorOptional.get().getDescription());
    }

    @Test
    public void shouldReturnErrorWhenCountryIsEmpty() {
        when(agreement.getCountry()).thenReturn("");
        when(errorFactory.buildError("ERROR_CODE_10"))
                .thenReturn(new ValidationErrorDTO("ERROR_CODE_10", "Country must be provided when TRAVEL_MEDICAL is selected"));
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement);
        assertTrue(errorOptional.isPresent());
        assertEquals("ERROR_CODE_10", errorOptional.get().getErrorCode());
        assertEquals("Country must be provided when TRAVEL_MEDICAL is selected", errorOptional.get().getDescription());
    }
}

