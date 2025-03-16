package org.javaguru.travel.insurance.core.repositories.entities;

import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
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
import java.util.List;
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
    @DisplayName("Test: Can save and find AgreementEntity by UUID")
    public void shouldSaveAndFindAgreementEntityByUuid() {
        var uuid = UUID.randomUUID().toString();
        var country = "JAPAN";
        var dateFrom = createDate("29.05.2050");
        var dateTo = createDate("29.05.2030");
        var premium = BigDecimal.valueOf(1000);

        AgreementEntity agreementEntity = new AgreementEntity();
        agreementEntity.setUuid(uuid);
        agreementEntity.setAgreementDateFrom(dateFrom);
        agreementEntity.setAgreementDateTo(dateTo);
        agreementEntity.setCountry(country);
        agreementEntity.setAgreementPremium(premium);
        repository.save(agreementEntity);

        Optional<AgreementEntity> foundAgreement = repository.findByUuid(uuid);
        assertTrue(foundAgreement.isPresent());
        assertEquals(foundAgreement.get().getUuid(), uuid);
        assertEquals(foundAgreement.get().getCountry(), country);
        assertEquals(foundAgreement.get().getAgreementDateFrom(), dateFrom);
        assertEquals(foundAgreement.get().getAgreementDateTo(), dateTo);
        assertEquals(foundAgreement.get().getAgreementPremium(), premium);
    }

    @Test
    @DisplayName("Test: Can't find AgreementEntity with non-existing UUID")
    public void shouldNotFindAgreementEntityWithFakeUuid() {
        Optional<AgreementEntity> foundAgreement = repository.findByUuid("fake-uuid");
        assertTrue(foundAgreement.isEmpty());
    }

    @Test
    @DisplayName("Test: Get all AgreementEntity UUIDs")
    public void shouldGetAllAgreementUuids() {
        AgreementEntity agreement1 = new AgreementEntity();
        agreement1.setUuid(UUID.randomUUID().toString());
        agreement1.setCountry("JAPAN");
        agreement1.setAgreementDateFrom(createDate("01.01.2040"));
        agreement1.setAgreementDateTo(createDate("01.01.2050"));
        agreement1.setAgreementPremium(BigDecimal.valueOf(1500));
        repository.save(agreement1);

        AgreementEntity agreement2 = new AgreementEntity();
        agreement2.setUuid(UUID.randomUUID().toString());
        agreement2.setCountry("SPAIN");
        agreement2.setAgreementDateFrom(createDate("01.02.2040"));
        agreement2.setAgreementDateTo(createDate("01.02.2050"));
        agreement2.setAgreementPremium(BigDecimal.valueOf(2000));
        repository.save(agreement2);

        List<String> uuids = repository.getNotExportedAgreementUuids();
        assertNotNull(uuids);
        assertTrue(uuids.contains(agreement1.getUuid()));
        assertTrue(uuids.contains(agreement2.getUuid()));
    }

    private Date createDate(String dateStr) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
