package org.javaguru.travel.insurance.core.repositories.entities;

import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.domain.entities.AgreementPersonEntity;
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
class AgreementPersonEntityRepositoryTest {

    @Autowired
    private AgreementPersonEntityRepository repository;

    @Autowired
    private AgreementEntityRepository agreementEntityRepository;

    @Autowired
    private PersonEntityRepository personEntityRepository;

    @Test
    @DisplayName("Test: AgreementPersonEntity repository is injected and not null")
    public void injectedRepositoryIsNotNull() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test: Can save and find AgreementPersonEntity by agreement and person")
    public void shouldSaveAndFindAgreementPersonEntity() {
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

        String medicalRiskLimitLevel = "LEVEL_10000";
        AgreementPersonEntity agreementPersonEntity = new AgreementPersonEntity();
        agreementPersonEntity.setAgreement(savedAgreementEntity);
        agreementPersonEntity.setPerson(savedPersonEntity);
        agreementPersonEntity.setMedicalRiskLimitLevel(medicalRiskLimitLevel);

        AgreementPersonEntity savedAgreementPersonEntity = repository.save(agreementPersonEntity);

        Optional<AgreementPersonEntity> foundAgreementPerson = repository.findById(savedAgreementPersonEntity.getId());

        assertTrue(foundAgreementPerson.isPresent());
        assertEquals(foundAgreementPerson.get().getAgreement().getId(), savedAgreementEntity.getId());
        assertEquals(foundAgreementPerson.get().getPerson().getId(), savedPersonEntity.getId());
        assertEquals(foundAgreementPerson.get().getMedicalRiskLimitLevel(), medicalRiskLimitLevel);
    }

    @Test
    @DisplayName("Test: Can't find AgreementPersonEntity with non-existing ID")
    public void shouldNotFindAgreementPersonEntityWithFakeId() {
        Optional<AgreementPersonEntity> foundAgreementPerson = repository.findById(9999L);
        assertTrue(foundAgreementPerson.isEmpty());
    }

    private Date createDate(String dateStr) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

