package org.javaguru.travel.insurance.core.services;



import org.javaguru.travel.insurance.core.underwriting.TravelPremiumCalculationResult;
import org.javaguru.travel.insurance.core.underwriting.TravelPremiumUnderwriting;
import org.javaguru.travel.insurance.core.validations.TravelCalculatePremiumRequestValidator;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumRequestV1;
import org.javaguru.travel.insurance.dto.v1.TravelCalculatePremiumResponseV1;
import org.javaguru.travel.insurance.dto.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelCalculatePremiumServiceImplTest {

    @Mock
    private TravelCalculatePremiumRequestValidator requestValidator;
    @Mock
    private TravelPremiumUnderwriting premiumUnderwriting;
    @Mock
    private TravelCalculatePremiumRequestV1 request;
    @Mock
    private TravelPremiumCalculationResult calculationResult;
    @InjectMocks
    private TravelCalculatePremiumServiceImpl service;

    private List<ValidationError> createValidationErrorList() {
        return List.of(new ValidationError("field", "errorMessage"));
    }
    private Date createDate(String str) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void shouldReturnResponseWithErrors() {
        List<ValidationError> errors = createValidationErrorList();
        when(requestValidator.validate(request)).thenReturn(errors);
        TravelCalculatePremiumResponseV1 response = service.calculatePremium(request);
        assertTrue(response.hasErrors());
    }

    @Test
    public void shouldReturnResponseWithValidationErrors() {
        List<ValidationError> errors = createValidationErrorList();
        when(requestValidator.validate(request)).thenReturn(errors);
        TravelCalculatePremiumResponseV1 response = service.calculatePremium(request);
        assertEquals(response.getErrors().size(), 1);
        assertEquals(response.getErrors().get(0).errorCode(), "field");
        assertEquals(response.getErrors().get(0).description(), "errorMessage");
    }

    @Test
    public void shouldNotInvokeDateTimeUtilWhenValidationErrors() {
        List<ValidationError> errors = createValidationErrorList();
        when(requestValidator.validate(request)).thenReturn(errors);
        TravelCalculatePremiumResponseV1 response = service.calculatePremium(request);
        assertEquals(response.getErrors().size(), 1);
        assertEquals(response.getErrors().get(0).errorCode(), "field");
        assertEquals(response.getErrors().get(0).description(), "errorMessage");
    }
    @Test
    public void shouldReturnResponseWithCorrectPersonFirstName() {
        when(request.getPersonFirstName()).thenReturn("personFirstName");
        when(requestValidator.validate(request)).thenReturn(List.of());
        when(premiumUnderwriting.calculatePremium(request)).thenReturn(calculationResult);
        TravelCalculatePremiumResponseV1 response = service.calculatePremium(request);
        assertEquals(response.getPersonFirstName(), "personFirstName");
    }
    @Test
    public void shouldReturnResponseWithCorrectPersonLastName() {
        when(request.getPersonLastName()).thenReturn("personLastName");
        when(requestValidator.validate(request)).thenReturn(List.of());
        when(premiumUnderwriting.calculatePremium(request)).thenReturn(calculationResult);
        TravelCalculatePremiumResponseV1 response = service.calculatePremium(request);
        assertEquals(response.getPersonLastName(), "personLastName");
    }
    @Test
    public void shouldReturnResponseWithCorrectAgreementDateFrom() {
        Date dateFrom = new Date();
        when(request.getAgreementDateFrom()).thenReturn(dateFrom);
        when(requestValidator.validate(request)).thenReturn(List.of());
        when(premiumUnderwriting.calculatePremium(request)).thenReturn(calculationResult);
        TravelCalculatePremiumResponseV1 response = service.calculatePremium(request);
        assertEquals(response.getAgreementDateFrom(), dateFrom);
    }
    @Test
    public void shouldReturnResponseWithCorrectAgreementDateTo() {
        Date dateTo = new Date();
        when(request.getAgreementDateTo()).thenReturn(dateTo);
        when(requestValidator.validate(request)).thenReturn(List.of());
        when(premiumUnderwriting.calculatePremium(request)).thenReturn(calculationResult);
        TravelCalculatePremiumResponseV1 response = service.calculatePremium(request);
        assertEquals(response.getAgreementDateTo(), dateTo);
    }
    @Test
    public void shouldReturnResponseWithCorrectAgreementPrice() {
        when(request.getAgreementDateFrom()).thenReturn(createDate("01.01.2023"));
        when(request.getAgreementDateTo()).thenReturn(createDate("10.01.2023"));
        when(requestValidator.validate(request)).thenReturn(List.of());
        TravelPremiumCalculationResult premiumCalculationResult = new TravelPremiumCalculationResult(new BigDecimal(9), null);
        when(premiumUnderwriting.calculatePremium(request)).thenReturn(premiumCalculationResult);
        TravelCalculatePremiumResponseV1 response = service.calculatePremium(request);
        assertEquals(response.getAgreementPremium(), new BigDecimal(9));
    }
}



