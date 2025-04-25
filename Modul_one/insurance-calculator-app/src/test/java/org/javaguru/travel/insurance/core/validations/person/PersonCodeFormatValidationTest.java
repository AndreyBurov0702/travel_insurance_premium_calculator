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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PersonCodeFormatValidationTest {

    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private AgreementDTO agreement;

    @Mock
    private PersonDTO person;

    @InjectMocks
    private PersonCodeFormatValidation validation;

    @Test
    public void shouldNotReturnErrorWhenPersonCodeIsNull() {
        when(person.getPersonCode()).thenReturn(null);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }

    @Test
    public void shouldNotReturnErrorWhenPersonCodeIsEmpty() {
        when(person.getPersonCode()).thenReturn("");
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }

    @Test
    public void shouldNotReturnErrorWhenPersonCodeIsRightFormatted() {
        when(person.getPersonCode()).thenReturn("123456-12345");
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }

    @Test
    public void shouldReturnErrorWhenPersonCodeIsNotRightFormatted() {
        when(person.getPersonCode()).thenReturn("a123456-12345");
        ValidationErrorDTO validationError = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError(eq("ERROR_CODE_21"), anyList())).thenReturn(validationError);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }
}
