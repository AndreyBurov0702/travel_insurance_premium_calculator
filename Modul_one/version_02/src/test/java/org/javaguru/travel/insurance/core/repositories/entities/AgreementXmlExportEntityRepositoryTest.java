package org.javaguru.travel.insurance.core.repositories.entities;

import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.domain.entities.AgreementXmlExportEntity;
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
class AgreementXmlExportEntityRepositoryTest {

    @Autowired
    private AgreementXmlExportEntityRepository repository;
    @Autowired
    private AgreementEntityRepository agreementRepository;

    @Test
    @DisplayName("Test: AgreementXmlExportEntity repository is injected and not null")
    public void injectedRepositoryAreNotNull() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test: Can save and find AgreementXmlExportEntity by ID")
    public void shouldSaveAndFindAgreementXmlExportEntityById() {
        AgreementEntity agreement = new AgreementEntity();
        agreement.setUuid(UUID.randomUUID().toString());
        agreement.setCountry("JAPAN");
        agreement.setAgreementDateFrom(createDate("01.01.2040"));
        agreement.setAgreementDateTo(createDate("01.01.2050"));
        agreement.setAgreementPremium(BigDecimal.valueOf(1000));
        agreementRepository.save(agreement);

        var entity = new AgreementXmlExportEntity();
        entity.setAgreementUuid(agreement.getUuid());
        entity.setAlreadyExported(false);
        repository.save(entity);

        Optional<AgreementXmlExportEntity> foundEntity = repository.findById(entity.getId());
        assertTrue(foundEntity.isPresent());
        assertEquals(entity.getAgreementUuid(), foundEntity.get().getAgreementUuid());
        assertEquals(entity.getAlreadyExported(), foundEntity.get().getAlreadyExported());
    }

        @Test
    @DisplayName("Test: Can't find AgreementXmlExportEntity with non-existing ID")
    public void shouldNotFindAgreementXmlExportEntityWithFakeId() {
        Optional<AgreementXmlExportEntity> foundEntity = repository.findById(999L);
        assertTrue(foundEntity.isEmpty());
    }

    private Date createDate(String dateStr) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
