package org.javaguru.travel.insurance.core.validations.agreement;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.util.DateTimeUtil;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgreementDateFromInFutureValidationTest {
    @Mock
    private AgreementDTO agreement;
    @Mock
    private DateTimeUtil dateTimeUtil;
    @Mock
    private ValidationErrorDTO validationError;
    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private AgreementDateFromInFutureValidation validation;

    private Date createDate (String str) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void shouldReturnErrorWhenAgreementDateFromInThePast() {
        when(agreement.getAgreementDateFrom()).thenReturn(createDate("01.01.2022"));
        when(dateTimeUtil.getCurrentDateTime()).thenReturn(createDate("01.01.2023"));
        when(errorFactory.buildError("ERROR_CODE_1")).thenReturn(validationError);
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }
    @Test
    public void shouldNotReturnErrorWhenAgreementDateFromInFuture() {
        when(agreement.getAgreementDateFrom()).thenReturn(createDate("01.01.2025"));
        when(dateTimeUtil.getCurrentDateTime()).thenReturn(createDate("01.01.2023"));
        Optional<ValidationErrorDTO> errorOptional = validation.validate(agreement);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }
}
