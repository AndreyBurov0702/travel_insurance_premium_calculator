package org.javaguru.travel.insurance.core.repositories;

import org.javaguru.travel.insurance.core.domain.AgeCoefficient;
import org.javaguru.travel.insurance.core.domain.MedicalRiskLimitLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MedicalRiskLimitLevelRepositoryTest {
    @Autowired
    MedicalRiskLimitLevelRepository repository;

    @Test
    @DisplayName("Test: MedicalRiskLimitLevel repository should not be null")
    public void repositoryShouldNotBeNull() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test: Can find record by medicalRiskLimitLevelIc")
    public void shouldFindMedicalRiskLimitLevelByIc() {
        Optional<MedicalRiskLimitLevel> medicalRiskLimitLevel = repository.findByMedicalRiskLimitLevelIc("LEVEL_10000");
        assertTrue(medicalRiskLimitLevel.isPresent());
        assertEquals(medicalRiskLimitLevel.get().getCoefficient(), new BigDecimal("1.00"));
    }

    @Test
    @DisplayName("Test: Can't find record with non-existing medicalRiskLimitLevelIc")
    public void shouldNotFindMedicalRiskLimitLevelByFakeIc() {
        String fakeIc = "LEVEL_99999";
        Optional<MedicalRiskLimitLevel> resultOpt = repository.findByMedicalRiskLimitLevelIc(fakeIc);
        assertTrue(resultOpt.isEmpty());
    }
}



