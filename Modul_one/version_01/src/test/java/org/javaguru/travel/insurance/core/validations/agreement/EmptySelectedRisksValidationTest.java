package org.javaguru.travel.insurance.core.validations.agreement;

import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.javaguru.travel.insurance.dto.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmptySelectedRisksValidationTest {
    @Mock
    private TravelCalculatePremiumRequestV1 request;
    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private ValidationError validationError;

    @InjectMocks
    private EmptySelectedRisksValidation validation;

    @Test
    public void shouldReturnErrorWhenSelectedRisksIsNull() {
        when(request.getSelectedRisks()).thenReturn(null);
        when(errorFactory.buildError("ERROR_CODE_6")).thenReturn(validationError);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }
    @Test
    public void shouldReturnErrorWhenSelectedRisksIsEmpty() {
        when(request.getSelectedRisks()).thenReturn(List.of());
        when(errorFactory.buildError("ERROR_CODE_6")).thenReturn(validationError);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }
    @Test
    public void shouldNotReturnErrorWhenSelectedRisksIsNotEmpty() {
        when(request.getSelectedRisks()).thenReturn(List.of("TRAVEL_MEDICAL"));
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }
}
