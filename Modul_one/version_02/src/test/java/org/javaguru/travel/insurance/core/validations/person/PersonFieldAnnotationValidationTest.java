package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonFieldAnnotationValidationTest {

    @Mock
    private ValidationErrorFactory errorFactory;

    @Mock
    private AgreementDTO agreement;

    @Mock
    private PersonDTO person;

    @Mock
    private ValidationErrorDTO validationError;

    @InjectMocks
    private PersonFieldAnnotationValidation validation;

    @BeforeEach
    void setUp() {
        person = new PersonDTO();
    }

    @Test
    public void shouldNotReturnError() {
        List<ValidationErrorDTO> errors = validation.validateList(agreement, person);
        assertTrue(errors.isEmpty());
        verifyNoInteractions(errorFactory);
    }

    @Test
    public void shouldReturnErrorWhenPersonFirstNameIsTooLong() {
        person.setPersonFirstName("VasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasjaVasja");
        person.setPersonLastName("Pupkin");
        when(errorFactory.buildError(eq("ERROR_CODE_24"), anyList())).thenReturn(validationError);
        List<ValidationErrorDTO> errors = validation.validateList(agreement, person);
        assertFalse(errors.isEmpty());
        assertSame(errors.get(0), validationError);
    }

    @Test
    public void shouldReturnErrorWhenPersonLastNameIsTooLong() {
        person.setPersonFirstName("Vasja");
        person.setPersonLastName("PupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkinPupkin");
        when(errorFactory.buildError(eq("ERROR_CODE_24"), anyList())).thenReturn(validationError);
        List<ValidationErrorDTO> errors = validation.validateList(agreement, person);
        assertFalse(errors.isEmpty());
        assertSame(errors.get(0), validationError);
    }

}