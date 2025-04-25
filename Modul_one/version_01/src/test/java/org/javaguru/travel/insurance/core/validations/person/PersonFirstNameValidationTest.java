package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.javaguru.travel.insurance.dto.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonFirstNameValidationTest {

    @Mock
    private TravelCalculatePremiumRequestV1 request;
    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private ValidationError validationError;
    @InjectMocks
    private PersonFirstNameValidation validation;
    @Test
    public void shouldReturnErrorWhenFirstNameIsNull() {
        when(request.getPersonFirstName()).thenReturn(null);
        when(errorFactory.buildError("ERROR_CODE_7")).thenReturn(validationError);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }
    @Test
    public void shouldReturnErrorWhenFirstNameIsEmpty() {
        when(request.getPersonFirstName()).thenReturn("");
        when(errorFactory.buildError("ERROR_CODE_7")).thenReturn(validationError);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }
    @Test
    public void shouldReturnNoErrorsWhenFirstNameIsProvided() {
        when(request.getPersonFirstName()).thenReturn("Andrey");
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }
}
