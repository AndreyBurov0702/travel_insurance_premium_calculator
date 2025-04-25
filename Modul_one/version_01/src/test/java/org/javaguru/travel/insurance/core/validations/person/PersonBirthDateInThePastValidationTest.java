package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.util.DateTimeUtil;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.javaguru.travel.insurance.dto.ValidationError;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonBirthDateInThePastValidationTest {
    @Mock
    private TravelCalculatePremiumRequestV1 request;
    @Mock
    private DateTimeUtil dateTimeUtil;
    @Mock private ValidationErrorFactory errorFactory;
    @Mock
    private ValidationError validationError;

    @InjectMocks
    private PersonBirthDateInThePastValidation validation;

    private Date createDate (String str) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void shouldReturnErrorWhenPersonBirthDateInTheFuture() {
        when(request.getPersonBirthDate()).thenReturn(createDate("01.01.2030"));
        when(dateTimeUtil.getCurrentDateTime()).thenReturn(createDate("01.01.2023"));
        when(errorFactory.buildError("ERROR_CODE_12")).thenReturn(validationError);
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isPresent());
        assertSame(errorOptional.get(), validationError);
    }
    @Test
    public void shouldNotReturnErrorWhenPersonBirthDateDateInThePast() {
        when(request.getPersonBirthDate()).thenReturn(createDate("01.01.2020"));
        when(dateTimeUtil.getCurrentDateTime()).thenReturn(createDate("01.01.2023"));
        Optional<ValidationError> errorOptional = validation.validate(request);
        assertTrue(errorOptional.isEmpty());
        verifyNoInteractions(errorFactory);
    }
}
