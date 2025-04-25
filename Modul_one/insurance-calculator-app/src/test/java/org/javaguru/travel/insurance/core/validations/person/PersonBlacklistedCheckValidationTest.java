package org.javaguru.travel.insurance.core.validations.person;


import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.blacklist.BlackListPersonCheckService;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonBlacklistedCheckValidationTest {

    @Mock
    private BlackListPersonCheckService blackListPersonCheckService;

    @Mock
    private ValidationErrorFactory errorFactory;

    @Mock
    private AgreementDTO agreement;

    @Mock
    private PersonDTO person;

    @Mock
    private ValidationErrorDTO validationError;

    @InjectMocks
    private PersonBlacklistedCheckValidation validation;

    @Test
    void shouldNotReturnErrorWhenFirstNameIsNull() {
        when(person.getPersonFirstName()).thenReturn(null);

        Optional<ValidationErrorDTO> result = validation.validate(agreement, person);

        assertTrue(result.isEmpty());
        verifyNoInteractions(blackListPersonCheckService);
        verifyNoInteractions(errorFactory);
    }

    @Test
    void shouldNotReturnErrorWhenLastNameIsBlank() {
        when(person.getPersonFirstName()).thenReturn("Vasja");
        when(person.getPersonLastName()).thenReturn(" ");
        Optional<ValidationErrorDTO> result = validation.validate(agreement, person);
        assertTrue(result.isEmpty());
        verifyNoInteractions(blackListPersonCheckService);
        verifyNoInteractions(errorFactory);
    }

    @Test
    void shouldNotReturnErrorWhenPersonCodeIsBlank() {
        when(person.getPersonFirstName()).thenReturn("Vasja");
        when(person.getPersonLastName()).thenReturn("Pupkin");
        when(person.getPersonCode()).thenReturn(" ");
        Optional<ValidationErrorDTO> result = validation.validate(agreement, person);
        assertTrue(result.isEmpty());
        verifyNoInteractions(blackListPersonCheckService);
        verifyNoInteractions(errorFactory);
    }

    @Test
    void shouldReturnErrorWhenPersonIsBlacklisted() {
        when(person.getPersonFirstName()).thenReturn("Vasja");
        when(person.getPersonLastName()).thenReturn("Pupkin");
        when(person.getPersonCode()).thenReturn("123456-12345");
        when(blackListPersonCheckService.isPersonBlacklisted(person)).thenReturn(true);
        when(errorFactory.buildError(eq("ERROR_CODE_25"), anyList())).thenReturn(validationError);

        Optional<ValidationErrorDTO> result = validation.validate(agreement, person);

        assertTrue(result.isPresent());
        assertSame(validationError, result.get());
    }

    @Test
    void shouldReturnEmptyWhenPersonIsNotBlacklisted() {
        when(person.getPersonFirstName()).thenReturn("Andrey");
        when(person.getPersonLastName()).thenReturn("Burov");
        when(person.getPersonCode()).thenReturn("654321-54321");
        when(blackListPersonCheckService.isPersonBlacklisted(person)).thenReturn(false);

        Optional<ValidationErrorDTO> result = validation.validate(agreement, person);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnErrorWhenServiceThrowsException() {
        when(person.getPersonFirstName()).thenReturn("Vasja");
        when(person.getPersonLastName()).thenReturn("Pupkin");
        when(person.getPersonCode()).thenReturn("123456-12345");
        when(blackListPersonCheckService.isPersonBlacklisted(person)).thenThrow(new RuntimeException("Service unavailable"));
        when(errorFactory.buildError("ERROR_CODE_26")).thenReturn(validationError);

        Optional<ValidationErrorDTO> result = validation.validate(agreement, person);

        assertTrue(result.isPresent());
        assertSame(validationError, result.get());
    }
}