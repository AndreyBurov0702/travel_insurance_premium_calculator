package org.javaguru.travel.insurance.core.repositories.entities;

import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AgreementEntityRepositoryTest {

    @Autowired
    private AgreementEntityRepository repository;

    @Test
    @DisplayName("Test: AgreementEntity repository is injected and not null")
    public void injectedRepositoryAreNotNull() {
        assertNotNull(repository);
    }
    @Test
    @DisplayName("Test: Can save and find AgreementEntity by country and dates")
    public void shouldSaveAndFindAgreementEntity() {
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
        repository.save(agreementEntity);

        Optional<AgreementEntity> foundAgreement = repository.findById(agreementEntity.getId());
        assertTrue(foundAgreement.isPresent());
        assertEquals(foundAgreement.get().getCountry(), country);
        assertEquals(foundAgreement.get().getAgreementDateFrom(), dateFrom);
        assertEquals(foundAgreement.get().getAgreementDateTo(), dateTo);
        assertEquals(foundAgreement.get().getAgreementPremium(), premium);
    }

    @Test
    @DisplayName("Test: Can't find AgreementEntity with non-existing ID")
    public void shouldNotFindAgreementEntityWithFakeId() {
        Optional<AgreementEntity> foundAgreement = repository.findById(9999L);
        assertTrue(foundAgreement.isEmpty());
    }

    private Date createDate(String dateStr) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
