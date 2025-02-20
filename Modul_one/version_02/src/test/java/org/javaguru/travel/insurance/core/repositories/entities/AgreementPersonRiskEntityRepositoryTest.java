package org.javaguru.travel.insurance.core.repositories.entities;

import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.domain.entities.AgreementPersonEntity;
import org.javaguru.travel.insurance.core.domain.entities.AgreementPersonRiskEntity;
import org.javaguru.travel.insurance.core.domain.entities.PersonEntity;
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
class AgreementPersonRiskEntityRepositoryTest {
    @Autowired
    private PersonEntityRepository personEntityRepository;
    @Autowired
    private AgreementPersonRiskEntityRepository repository;
    @Autowired
    private AgreementPersonEntityRepository agreementPersonEntityRepository;
    @Autowired
    private AgreementEntityRepository agreementEntityRepository;

    @Test
    @DisplayName("Test: AgreementPersonRiskEntity repository is injected and not null")
    public void injectedRepositoryIsNotNull() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test: Can save and find AgreementPersonRiskEntity by id")
    public void shouldSaveAndFindAgreementPersonRiskEntity() {
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

        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName("Vasja");
        personEntity.setLastName("Pupkin");
        personEntity.setPersonCode("123456-12345");
        personEntity.setBirthDate(createDate("29.05.2000"));
        PersonEntity savedPersonEntity = personEntityRepository.save(personEntity);

        AgreementPersonEntity agreementPersonEntity = new AgreementPersonEntity();
        agreementPersonEntity.setAgreement(savedAgreementEntity);
        agreementPersonEntity.setPerson(personEntity);
        agreementPersonEntity.setMedicalRiskLimitLevel("TRAVEL_MEDICAL");
        agreementPersonEntity = agreementPersonEntityRepository.save(agreementPersonEntity);

        String riskIc = "TRAVEL_MEDICAL";
        BigDecimal riskPremium = BigDecimal.valueOf(100);

        AgreementPersonRiskEntity agreementPersonRiskEntity = new AgreementPersonRiskEntity();
        agreementPersonRiskEntity.setAgreementPerson(agreementPersonEntity);
        agreementPersonRiskEntity.setRiskIc(riskIc);
        agreementPersonRiskEntity.setPremium(riskPremium);

        AgreementPersonRiskEntity savedRiskEntity = repository.save(agreementPersonRiskEntity);

        Optional<AgreementPersonRiskEntity> foundRisk = repository.findById(savedRiskEntity.getId());
        assertTrue(foundRisk.isPresent());
        assertEquals(foundRisk.get().getAgreementPerson().getId(), agreementPersonEntity.getId());
        assertEquals(foundRisk.get().getRiskIc(), riskIc);
        assertEquals(foundRisk.get().getPremium(), riskPremium);
    }

    @Test
    @DisplayName("Test: Can't find AgreementPersonRiskEntity with non-existing ID")
    public void shouldNotFindAgreementPersonRiskEntityWithFakeId() {
        Optional<AgreementPersonRiskEntity> foundRisk = repository.findById(9999L);
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
