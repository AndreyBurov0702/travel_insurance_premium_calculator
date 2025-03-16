package org.javaguru.travel.insurance.core.repositories.entities;


import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.domain.entities.SelectedRiskEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class SelectedRiskEntityRepositoryTest {
    @Autowired
    private SelectedRiskEntityRepository repository;
    @Autowired
    private AgreementEntityRepository agreementEntityRepository;


    @Test
    @DisplayName("Test: SelectedRiskEntity repository is injected and not null")
    public void injectedRepositoryIsNotNull() {
        assertNotNull(repository);
    }
    @Test
    @DisplayName("Test: Can save and find SelectedRiskEntity by agreement and risk IC")
    public void shouldSaveAndFindSelectedRiskEntity() {
        var country = "JAPAN";
        var dateFrom = createDate("29.05.2050");
        var dateTo = createDate("29.05.2030");
        var premium = BigDecimal.valueOf(1000);

        AgreementEntity agreementEntity = new AgreementEntity();
        agreementEntity.setUuid(UUID.randomUUID().toString());
        agreementEntity.setAgreementDateFrom(dateFrom);
        agreementEntity.setAgreementDateTo(dateTo);
        agreementEntity.setCountry(country);
        agreementEntity.setAgreementPremium(premium);

        AgreementEntity savedAgreementEntity = agreementEntityRepository.save(agreementEntity);

        String riskIc = "TRAVEL_MEDICAL";
        SelectedRiskEntity selectedRiskEntity = new SelectedRiskEntity();
        selectedRiskEntity.setAgreement(savedAgreementEntity);  // связываем с сохраненным AgreementEntity
        selectedRiskEntity.setRiskIc(riskIc);

        SelectedRiskEntity savedRiskEntity = repository.save(selectedRiskEntity);

        Optional<SelectedRiskEntity> foundRisk = repository.findById(savedRiskEntity.getId());
        assertTrue(foundRisk.isPresent());
        assertEquals(foundRisk.get().getAgreement().getId(), savedAgreementEntity.getId());
        assertEquals(foundRisk.get().getRiskIc(), riskIc);
    }

    @Test
    @DisplayName("Test: Can't find SelectedRiskEntity with non-existing ID")
    public void shouldNotFindSelectedRiskEntityWithFakeId() {
        Optional<SelectedRiskEntity> foundRisk = repository.findById(9999L);
        assertTrue(foundRisk.isEmpty());
    }

    private Date createDate(String dateStr) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
