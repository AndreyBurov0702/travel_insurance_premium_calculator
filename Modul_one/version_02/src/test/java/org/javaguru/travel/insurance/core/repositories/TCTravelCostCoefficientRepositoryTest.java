package org.javaguru.travel.insurance.core.repositories;

import org.javaguru.travel.insurance.core.domain.TCTravelCostCoefficient;
import org.junit.jupiter.api.BeforeEach;
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
class TCTravelCostCoefficientRepositoryTest {

    @Autowired
    private TCTravelCostCoefficientRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }
    @Test
    @DisplayName("Test: TCTravelCostCoefficient repository should not be null")
    void repositoryShouldNotBeNull() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test: Can find coefficient by travel cost")
    void shouldFindCoefficientByTravelCost() {
        TCTravelCostCoefficient coefficient = new TCTravelCostCoefficient();
        coefficient.setTravelCostFrom(new BigDecimal("20000"));
        coefficient.setTravelCostTo(new BigDecimal("1000000"));
        coefficient.setCoefficient(new BigDecimal("500.0"));
        repository.save(coefficient);

        Optional<TCTravelCostCoefficient> result = repository.findCoefficient(new BigDecimal("20000"));

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("500.0"), result.get().getCoefficient());
    }

    @Test
    @DisplayName("Test: Can't find coefficient for non-existing travel cost")
    void shouldNotFindCoefficientForNonExistingTravelCost() {
        Optional<TCTravelCostCoefficient> result = repository.findCoefficient(new BigDecimal("1000001.00"));
        assertTrue(result.isEmpty());
    }
}
