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
class PersonLastNameFormatValidationTest {

    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private AgreementDTO agreement;

    @Mock
    private PersonDTO person;

    @InjectMocks
    private PersonLastNameFormatValidation validation;

    @Test
    public void shouldNotReturnErrorWhenPersonLastNameIsNull() {
        when(person.getPersonLastName()).thenReturn(null);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }

    @Test
    public void shouldNotReturnErrorWhenPersonLastNameIsEmpty() {
        when(person.getPersonLastName()).thenReturn("");
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }

    @Test
    public void shouldNotReturnErrorWhenPersonLastNameIsRightFormatted() {
        when(person.getPersonLastName()).thenReturn("John Smith-Jones");
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }

    @Test
    public void shouldReturnErrorWhenPersonLastNameIsNotRightFormatted() {
        when(person.getPersonLastName()).thenReturn("John Smith-Jones 1987");
        ValidationErrorDTO validationError = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError(eq("ERROR_CODE_23"), anyList())).thenReturn(validationError);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement, person);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }
}
