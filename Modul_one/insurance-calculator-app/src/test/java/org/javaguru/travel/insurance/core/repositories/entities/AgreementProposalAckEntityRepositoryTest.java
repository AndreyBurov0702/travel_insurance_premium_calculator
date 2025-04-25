package org.javaguru.travel.insurance.core.repositories.entities;

import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.domain.entities.AgreementProposalAckEntity;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AgreementProposalAckEntityRepositoryTest {

    @Autowired
    private AgreementProposalAckEntityRepository repository;

    @Autowired
    private AgreementEntityRepository agreementRepository;

    @Test
    @DisplayName("Test: AgreementProposalAckEntity repository is injected and not null")
    public void injectedRepositoryIsNotNull() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test: Can save and find AgreementProposalAckEntity by ID")
    public void shouldSaveAndFindAgreementProposalAckEntityById() {
        String uuid = UUID.randomUUID().toString();

        AgreementEntity agreement = new AgreementEntity();
        agreement.setUuid(uuid);
        agreement.setCountry("JAPAN");
        agreement.setAgreementDateFrom(createDate("01.01.2040"));
        agreement.setAgreementDateTo(createDate("01.01.2050"));
        agreement.setAgreementPremium(BigDecimal.valueOf(1000));

        agreementRepository.save(agreement);

        AgreementProposalAckEntity entity = new AgreementProposalAckEntity();
        entity.setAgreementUuid(uuid);
        entity.setAlreadyGenerated(true);
        entity.setProposalFilePath("/files/proposal.xml");

        repository.save(entity);

        Optional<AgreementProposalAckEntity> found = repository.findById(entity.getId());
        assertTrue(found.isPresent());
        assertEquals(uuid, found.get().getAgreementUuid());
    }

    @Test
    @DisplayName("Test: Can't find AgreementProposalAckEntity with non-existing ID")
    public void shouldNotFindAgreementProposalAckEntityWithFakeId() {
        Optional<AgreementProposalAckEntity> found = repository.findById(999L);
        assertTrue(found.isEmpty());
    }

    private Date createDate(String dateStr) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}