package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.command.TravelExportAgreementToXmlCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelExportAgreementToXmlCoreResult;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreResult;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementXmlExportEntityRepository;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TravelExportAgreementToXmlServiceImplTest {

    @Mock
    private TravelGetAgreementService agreementService;

    @Mock
    private AgreementXmlExportEntityRepository agreementXmlExportEntityRepository;

    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private TravelExportAgreementToXmlServiceImpl service;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "agreementExportPath", tempDir.toString());
    }

    @Test
    @DisplayName("Test: Should export agreement to XML successfully")
    void shouldExportAgreementToXmlSuccessfully() throws Exception {
        String agreementUuid = "5b16ef02-aad1-480c-9bff-8d78c9242b3b";
        TravelExportAgreementToXmlCoreCommand command = mock(TravelExportAgreementToXmlCoreCommand.class);
        when(command.getUuid()).thenReturn(agreementUuid);
        AgreementDTO agreement = new AgreementDTO();
        TravelGetAgreementCoreResult agreementResult = new TravelGetAgreementCoreResult(Collections.emptyList(), agreement);
        when(agreementService.getAgreement(any())).thenReturn(agreementResult);

        TravelExportAgreementToXmlCoreResult result = service.export(command);

        assertNotNull(result);
        Path xmlFilePath = tempDir.resolve("agreement-" + agreementUuid + ".xml");
        assertTrue(Files.exists(xmlFilePath));
        verify(agreementXmlExportEntityRepository).save(any());
    }

    @Test
    @DisplayName("Test: Should return error when export fails")
    void shouldReturnErrorWhenExportFails() {
        String agreementUuid = "agreement-5b16ef02-aad1-480c-9bff-8d78c9242b3b";
        TravelExportAgreementToXmlCoreCommand command = mock(TravelExportAgreementToXmlCoreCommand.class);
        when(command.getUuid()).thenReturn(agreementUuid);
        when(command.getUuid()).thenReturn(Collections.emptyList().toString());
        when(agreementService.getAgreement(any())).thenThrow(new RuntimeException("Service failure"));
        when(errorFactory.buildError("ERROR_CODE_20")).thenReturn(new ValidationErrorDTO("ERROR_CODE_20", "Can not export agreement to xml file!"));

        TravelExportAgreementToXmlCoreResult result = service.export(command);

        assertNotNull(result);
        assertEquals(1, result.getErrors().size());
        assertEquals("ERROR_CODE_20", result.getErrors().get(0).getErrorCode());
    }
}