package org.javaguru.travel.insurance.core.validations.agreement;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.domain.ClassifierValue;
import org.javaguru.travel.insurance.core.repositories.ClassifierValueRepository;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryValidationTest {

    @Mock
    private ClassifierValueRepository classifierValueRepository;
    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private AgreementDTO agreement;
    @Mock
    private ClassifierValue classifierValue;

    @InjectMocks
    private CountryValidation validation;

    @Test
    public void shouldNotReturnErrorWhenCountryNotBlankAndExistInDb() {
        when(agreement.getCountry()).thenReturn("USA");
        when(classifierValueRepository.findByClassifierTitleAndIc("COUNTRY", "USA"))
                .thenReturn(Optional.of(classifierValue));
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }
    @Test
    public void shouldReturnErrorWhenCountryNotBlankAndNotExistInDb() {
        when(agreement.getCountry()).thenReturn("UNKNOWN_COUNTRY");
        when(classifierValueRepository.findByClassifierTitleAndIc("COUNTRY", "UNKNOWN_COUNTRY"))
                .thenReturn(Optional.empty());
        ValidationErrorDTO validationError = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError(eq("ERROR_CODE_15"), any())).thenReturn(validationError);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement);
        assertTrue(errorOptional.isPresent());
        assertSame(validationError, errorOptional.get());
    }
    @Test
    public void shouldNotReturnErrorWhenCountryIsBlank() {
        when(agreement.getCountry()).thenReturn("");
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(classifierValueRepository, errorFactory);
    }
    @Test
    public void shouldNotReturnErrorWhenCountryIsNull() {
        when(agreement.getCountry()).thenReturn(null);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(classifierValueRepository, errorFactory);
    }
}
