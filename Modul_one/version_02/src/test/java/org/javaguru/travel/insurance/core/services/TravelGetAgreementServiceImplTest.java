package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreResult;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.TravelAgreementUuidValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelGetAgreementServiceImplTest {
    @Mock
    private TravelAgreementUuidValidator agreementUuidValidator;

    @Mock
    private AgreementDTOLoader agreementDTOLoader;

    @InjectMocks
    private TravelGetAgreementServiceImpl service;

    @Test
    public void shouldReturnValidationErrors() {
        String agreementUuid = "08060c1b-ddc3-4bac-8fbf-dc9f3554f859";
        TravelGetAgreementCoreCommand command = new TravelGetAgreementCoreCommand(agreementUuid);
        ValidationErrorDTO validationError = new ValidationErrorDTO("Error code", "Error description");
        when(agreementUuidValidator.validate(agreementUuid)).thenReturn(List.of(validationError));

        TravelGetAgreementCoreResult result = service.getAgreement(command);
        assertEquals(1, result.getErrors().size());
        assertEquals("Error code", result.getErrors().get(0).getErrorCode());
        assertEquals("Error description", result.getErrors().get(0).getDescription());
        verifyNoInteractions(agreementDTOLoader);
    }

    @Test
    public void shouldReturnAgreementWhenNoValidationErrors() {
        String agreementUuid = "08060c1b-ddc3-4bac-8fbf-dc9f3554f859";
        TravelGetAgreementCoreCommand command = new TravelGetAgreementCoreCommand(agreementUuid);
        when(agreementUuidValidator.validate(agreementUuid)).thenReturn(Collections.emptyList());
        AgreementDTO agreementDTO = new AgreementDTO();
        when(agreementDTOLoader.load(agreementUuid)).thenReturn(agreementDTO);

        TravelGetAgreementCoreResult result = service.getAgreement(command);
        assertNotNull(result.getAgreement());
        assertEquals(agreementDTO, result.getAgreement());
        verify(agreementDTOLoader).load(agreementUuid);
    }
}

