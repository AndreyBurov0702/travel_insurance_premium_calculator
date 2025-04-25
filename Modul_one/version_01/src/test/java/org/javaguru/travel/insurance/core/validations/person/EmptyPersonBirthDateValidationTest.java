package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.javaguru.travel.insurance.dto.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmptyPersonBirthDateValidationTest {
    @Mock
    private TravelCalculatePremiumRequestV1 request;
    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private EmptyPersonBirthDateValidation validation;

    @Test
    public void shouldReturnNoErrorWhenPersonBirthDateIsPresent() {
        when(request.getPersonBirthDate()).thenReturn(new Date());
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isEmpty());
    }
    @Test
    public void shouldReturnErrorWhenPersonBirthDateIsNull() {
        when(request.getPersonBirthDate()).thenReturn(null);
        when(errorFactory.buildError("ERROR_CODE_11"))
                .thenReturn(new ValidationError("ERROR_CODE_11", "Person Birth Date must be provided when TRAVEL_MEDICAL is selected"));
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isPresent());
        assertEquals("ERROR_CODE_11", errorOptional.get().errorCode());
        assertEquals("Person Birth Date must be provided when TRAVEL_MEDICAL is selected", errorOptional.get().description());
    }
}


