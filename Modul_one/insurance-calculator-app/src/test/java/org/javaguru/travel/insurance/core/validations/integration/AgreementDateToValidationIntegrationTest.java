package org.javaguru.travel.insurance.core.validations.integration;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.TravelAgreementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AgreementDateToValidationIntegrationTest {

    @Autowired private TravelAgreementValidator validator;

    @Test
    public void shouldReturnErrorWhenDateToIsNull() {
        AgreementDTO agreement = AgreementDTO.builder()
                .agreementDateFrom(createDate("01.01.2030"))
                .agreementDateTo(null)
                .country("SPAIN")
                .selectedRisks(List.of("TRAVEL_MEDICAL"))
                .persons(List.of(PersonDTO.builder()
                        .personFirstName("Vasja")
                        .personLastName("Pupkin")
                        .personCode("123456-12345")
                        .personBirthDate(createDate("01.01.2000"))
                        .medicalRiskLimitLevel("LEVEL_10000")
                        .build())
                ).build();
        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 1);
        assertEquals(errors.get(0).getErrorCode(), "ERROR_CODE_4");
        assertEquals(errors.get(0).getDescription(), "Field agreementDateTo must not be empty!");
    }

    @Test
    public void shouldReturnErrorWhenDateToIsInThePast() {
        AgreementDTO agreement = AgreementDTO.builder()
                .agreementDateFrom(createDate("29.05.2027"))
                .agreementDateTo(createDate("01.01.2020"))
                .country("SPAIN")
                .selectedRisks(List.of("TRAVEL_MEDICAL"))
                .persons(List.of(PersonDTO.builder()
                        .personFirstName("Vasja")
                        .personLastName("Pupkin")
                        .personCode("123456-12345")
                        .personBirthDate(createDate("01.01.2000"))
                        .medicalRiskLimitLevel("LEVEL_10000")
                        .build())
                ).build();
        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 2);
        assertEquals(errors.get(0).getErrorCode(), "ERROR_CODE_3");
        assertEquals(errors.get(0).getDescription(), "Field agreementDateTo must be in the future!");
    }

    private Date createDate(String dateStr) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
