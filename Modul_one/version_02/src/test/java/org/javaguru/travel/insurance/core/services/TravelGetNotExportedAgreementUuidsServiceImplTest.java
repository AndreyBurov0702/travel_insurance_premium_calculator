package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.command.TravelGetNotExportedAgreementUuidsCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetNotExportedAgreementUuidsCoreResult;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelGetNotExportedAgreementUuidsServiceImplTest {
    @Mock
    private AgreementEntityRepository agreementEntityRepository;

    @InjectMocks
    private TravelGetNotExportedAgreementUuidsServiceImpl service;

    @Test
    @DisplayName("Test: Should return all agreement UUIDs")
    public void shouldReturnAllAgreementUuids() {
        List<String> mockUuids = List.of("uuid1", "uuid2", "uuid3");
        when(agreementEntityRepository.getNotExportedAgreementUuids()).thenReturn(mockUuids);

        TravelGetNotExportedAgreementUuidsCoreCommand command = new TravelGetNotExportedAgreementUuidsCoreCommand();
        TravelGetNotExportedAgreementUuidsCoreResult result = service.getAgreementUuids(command);

        assertNotNull(result);
        assertEquals(mockUuids, result.getAgreementUuids());
    }

    @Test
    @DisplayName("Test: Should return empty list when no agreements found")
    public void shouldReturnEmptyListWhenNoAgreementsFound() {
        when(agreementEntityRepository.getNotExportedAgreementUuids()).thenReturn(Collections.emptyList());

        TravelGetNotExportedAgreementUuidsCoreCommand command = new TravelGetNotExportedAgreementUuidsCoreCommand();
        TravelGetNotExportedAgreementUuidsCoreResult result = service.getAgreementUuids(command);

        assertNotNull(result);
        assertTrue(result.getAgreementUuids().isEmpty());
    }
}
