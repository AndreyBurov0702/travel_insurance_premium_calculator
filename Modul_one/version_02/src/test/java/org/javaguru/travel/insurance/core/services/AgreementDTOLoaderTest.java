package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementEntityRepository;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementPersonEntityRepository;
import org.javaguru.travel.insurance.core.repositories.entities.SelectedRiskEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgreementDTOLoaderTest {
    @Mock
    private AgreementEntityRepository agreementEntityRepository;
    @Mock
    private SelectedRiskEntityRepository selectedRiskEntityRepository;

    @Mock
    private AgreementPersonEntityRepository agreementPersonEntityRepository;

    @InjectMocks
    private AgreementDTOLoader agreementDTOLoader;

    @Test
    public void shouldReturnEmptyListForPersonsIfNoPersonsFound() {
        String uuid = "08060c1b-ddc3-4bac-8fbf-dc9f3554f859";

        AgreementEntity agreementEntity = new AgreementEntity();
        agreementEntity.setUuid(uuid);

        when(agreementEntityRepository.findByUuid(uuid)).thenReturn(Optional.of(agreementEntity));
        when(agreementPersonEntityRepository.findByAgreement(agreementEntity)).thenReturn(Collections.emptyList());

        AgreementDTO result = agreementDTOLoader.load(uuid);
        assertNotNull(result);
        assertEquals(uuid, result.getUuid());
        assertTrue(result.getPersons().isEmpty());
        assertNull(result.getCountry());
        assertNull(result.getAgreementPremium());
    }
}

