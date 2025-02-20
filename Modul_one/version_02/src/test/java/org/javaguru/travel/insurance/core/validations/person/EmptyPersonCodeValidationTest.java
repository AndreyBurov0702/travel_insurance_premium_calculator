package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmptyPersonCodeValidationTest {
    @Mock
    private PersonDTO person;
    @Mock
    private AgreementDTO agreement;
    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private ValidationErrorDTO validationError;

    @InjectMocks
    private EmptyPersonCodeValidation validation;

    @Test
    public void shouldReturnErrorWhenPersonCodeIsNull() {
        when(person.getPersonCode()).thenReturn(null);
        when(errorFactory.buildError("ERROR_CODE_16")).thenReturn(validationError);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }

    @Test
    public void shouldReturnErrorWhenPersonCodeIsEmpty() {
        when(person.getPersonCode()).thenReturn("");
        when(errorFactory.buildError("ERROR_CODE_16")).thenReturn(validationError);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }
    @Test
    public void shouldNotReturnErrorWhenPersonCodeIsPresent() {
        when(person.getPersonCode()).thenReturn("123456-12345");
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }
}
