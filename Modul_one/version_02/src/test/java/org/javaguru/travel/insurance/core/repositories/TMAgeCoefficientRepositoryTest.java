package org.javaguru.travel.insurance.core.repositories;

import org.javaguru.travel.insurance.core.domain.TMAgeCoefficient;
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
class TMAgeCoefficientRepositoryTest {

    @Autowired
    private TMAgeCoefficientRepository repository;

    @Test
    @DisplayName("Test: TMAgeCoefficient table is present")
    public void injectedRepositoryAreNotNull() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test: Can find record by Age")
    public void shouldFindAgeCoefficient() {
        Optional<TMAgeCoefficient> ageCoefficientOpt = repository.findCoefficient(11);
        assertTrue(ageCoefficientOpt.isPresent());
        assertEquals(ageCoefficientOpt.get().getCoefficient(), new BigDecimal("1.00"));
    }

    @Test
    @DisplayName("Test: Can't find record with Fake Age")
    public void shouldNotFindAgeCoefficient() {
        Optional<TMAgeCoefficient> ageCoefficientOpt = repository.findCoefficient(151);
        assertTrue(ageCoefficientOpt.isEmpty());
    }
}
