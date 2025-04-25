package org.javaguru.travel.insurance.core.validations;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
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
class TravelAgreementFieldValidatorTest {

    @Mock
    private AgreementDTO agreement;
    @Mock
    private TravelAgreementFieldValidation validation1;
    @Mock
    private TravelAgreementFieldValidation validation2;

    @Test
    public void shouldNotReturnErrors() {
        when(validation1.validate(agreement)).thenReturn(Optional.empty());
        when(validation1.validateList(agreement)).thenReturn(List.of());

        when(validation2.validate(agreement)).thenReturn(Optional.empty());
        when(validation2.validateList(agreement)).thenReturn(List.of());
        List<TravelAgreementFieldValidation> agreementValidations = List.of(validation1, validation2);
        var validator = new TravelAgreementFieldValidator(agreementValidations);

        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertTrue(errors.isEmpty());
    }
    @Test
    public void shouldReturnSingleAgreementErrors() {
        when(validation1.validate(agreement)).thenReturn(Optional.of(new ValidationErrorDTO()));

        when(validation2.validate(agreement)).thenReturn(Optional.of(new ValidationErrorDTO()));
        List<TravelAgreementFieldValidation> agreementValidations = List.of(validation1, validation2);
        var validator = new TravelAgreementFieldValidator(agreementValidations);

        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 2);
    }
    @Test
    public void shouldReturnListAgreementErrors() {
        when(validation1.validate(agreement)).thenReturn(Optional.empty());
        when(validation1.validateList(agreement)).thenReturn(List.of(new ValidationErrorDTO()));

        when(validation2.validate(agreement)).thenReturn(Optional.empty());
        when(validation2.validateList(agreement)).thenReturn(List.of(new ValidationErrorDTO()));
        List<TravelAgreementFieldValidation> agreementValidations = List.of(validation1, validation2);
        var validator = new TravelAgreementFieldValidator(agreementValidations);

        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 2);
    }
}