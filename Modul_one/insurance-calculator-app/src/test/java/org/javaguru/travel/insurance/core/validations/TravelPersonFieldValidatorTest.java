package org.javaguru.travel.insurance.core.validations;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
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
class TravelPersonFieldValidatorTest {

    @Mock
    private PersonDTO person;
    @Mock
    AgreementDTO agreement;
    @Mock
    private TravelPersonFieldValidation validation1;
    @Mock
    private TravelPersonFieldValidation validation2;

    @Test
    public void shouldNotReturnErrors() {
        when(agreement.getPersons()).thenReturn(List.of(person));
        when(validation1.validate(agreement, person)).thenReturn(Optional.empty());
        when(validation1.validateList(agreement, person)).thenReturn(List.of());
        when(validation2.validate(agreement, person)).thenReturn(Optional.empty());
        when(validation2.validateList(agreement, person)).thenReturn(List.of());

        var validator = new TravelPersonFieldValidator(List.of(validation1, validation2));

        List<ValidationErrorDTO> errors = validator.validate(agreement);

        assertTrue(errors.isEmpty());
    }

    @Test
    public void shouldReturnSinglePersonErrors() {
        when(agreement.getPersons()).thenReturn(List.of(person));
        when(validation1.validate(agreement, person)).thenReturn(Optional.of(new ValidationErrorDTO()));
        when(validation2.validate(agreement, person)).thenReturn(Optional.of(new ValidationErrorDTO()));

        var validator = new TravelPersonFieldValidator(List.of(validation1, validation2));
        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 2);
    }

    @Test
    public void shouldReturnListPersonErrors() {
        when(agreement.getPersons()).thenReturn(List.of(person));
        when(validation1.validate(agreement, person)).thenReturn(Optional.empty());
        when(validation1.validateList(agreement, person)).thenReturn(List.of(new ValidationErrorDTO()));
        when(validation2.validate(agreement, person)).thenReturn(Optional.empty());
        when(validation2.validateList(agreement, person)).thenReturn(List.of(new ValidationErrorDTO()));

        var validator = new TravelPersonFieldValidator(List.of(validation1, validation2));
        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 2);
    }
}