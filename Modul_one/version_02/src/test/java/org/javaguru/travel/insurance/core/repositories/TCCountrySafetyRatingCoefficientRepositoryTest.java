package org.javaguru.travel.insurance.core.repositories;

import org.javaguru.travel.insurance.core.domain.TCCountrySafetyRatingCoefficient;
import org.javaguru.travel.insurance.core.domain.TMCountryDefaultDayRate;
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
class TCCountrySafetyRatingCoefficientRepositoryTest {

    @Autowired
    private TCCountrySafetyRatingCoefficientRepository countrySafetyRatingCoefficientRepository;

    @Test
    public void injectedRepositoryAreNotNull(){
        assertNotNull(countrySafetyRatingCoefficientRepository);
    }
    @Test
    public void shouldFindForLatvia(){
        searchCountryDefaultDayRate("LATVIA", new BigDecimal("5.00"));
    }
    @Test
    public void shouldFindForSpain(){
        searchCountryDefaultDayRate("SPAIN", new BigDecimal("8.00"));
    }
    @Test
    public void shouldFindForJapan(){
        searchCountryDefaultDayRate("JAPAN", new BigDecimal("9.00"));
    }
    @Test
    public void shouldNotFindForUnknownCountry(){
        Optional<TCCountrySafetyRatingCoefficient> valueOptional = countrySafetyRatingCoefficientRepository.findByCountryIc("FAKE_COUNTRY");
        assertTrue(valueOptional.isEmpty());
    }
    private void searchCountryDefaultDayRate (String countryIc, BigDecimal dayRate){
        Optional<TCCountrySafetyRatingCoefficient> valueOptional = countrySafetyRatingCoefficientRepository.findByCountryIc(countryIc);
        assertTrue(valueOptional.isPresent());
        assertEquals(valueOptional.get().getCountryIc(), countryIc);
        assertEquals(valueOptional.get().getCoefficient(), dayRate);
        assertEquals(dayRate.stripTrailingZeros(), valueOptional.get().getCoefficient().stripTrailingZeros());
    }
}
