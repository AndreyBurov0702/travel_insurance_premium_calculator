package org.javaguru.travel.insurance.core.validations.integration;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.TravelAgreementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AgreementSelectedRiskValidationIntegrationTest {
    @Autowired
    private TravelAgreementValidator validator;

    @Test
    public void shouldReturnErrorWhenSelectedRisksIsNull() {
        AgreementDTO agreement = AgreementDTO.builder()
                .agreementDateFrom(createDate("01.01.2027"))
                .agreementDateTo(createDate("01.01.2030"))
                .country("JAPAN")
                .selectedRisks(List.of(""))
                .persons(List.of(PersonDTO.builder()
                        .personFirstName("Vasja")
                        .personLastName("Pupkin")
                        .personCode("123456-12345")
                        .personBirthDate(createDate("01.01.2000"))
                        .medicalRiskLimitLevel("LEVEL_10000")
                        .risks(List.of(RiskDTO.builder()
                                .riskIc("TRAVEL_EVACUATION")
                                .premium(BigDecimal.ONE)
                                .build()
                        )) // <-- Правильное закрытие списка risks
                        .build() // <-- Закрываем PersonDTO
                )) // <-- Закрываем List.of(PersonDTO)
                .build(); // <-- Закрываем AgreementDTO
        List<ValidationErrorDTO> errors = validator.validate(agreement);
        assertEquals(errors.size(), 1);
        assertEquals(errors.get(0).getErrorCode(), "ERROR_CODE_9");
        assertEquals(errors.get(0).getDescription(), "Risk Type ic =  not supported!");
    }

    private Date createDate (String str) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
