package org.javaguru.travel.insurance.core.repositories;

import org.javaguru.travel.insurance.core.domain.TMCountryDefaultDayRate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TMCountryDefaultDayRateRepositoryTest {

    @Autowired
    private TMCountryDefaultDayRateRepository countryDefaultDayRateRepository;

    @Test
    public void injectedRepositoryAreNotNull(){
        assertNotNull(countryDefaultDayRateRepository);
    }
    @Test
    public void shouldFindForLatvia(){
        searchCountryDefaultDayRate("LATVIA", new BigDecimal("1.00"));
    }
    @Test
    public void shouldFindForSpain(){
        searchCountryDefaultDayRate("SPAIN", new BigDecimal("2.50"));
    }
    @Test
    public void shouldFindForJapan(){
        searchCountryDefaultDayRate("JAPAN", new BigDecimal("3.50"));
    }
    @Test
    public void shouldNotFindForUnknownCountry(){
        Optional<TMCountryDefaultDayRate> valueOptional = countryDefaultDayRateRepository.findByCountryIc("FAKE_COUNTRY");
        assertTrue(valueOptional.isEmpty());
    }
    private void searchCountryDefaultDayRate (String countryIc, BigDecimal dayRate){
        Optional<TMCountryDefaultDayRate> valueOptional = countryDefaultDayRateRepository.findByCountryIc(countryIc);
        assertTrue(valueOptional.isPresent());
        assertEquals(valueOptional.get().getCountryIc(), countryIc);
        assertEquals(valueOptional.get().getDefaultDayRate(), dayRate);
        assertEquals(dayRate.stripTrailingZeros(), valueOptional.get().getDefaultDayRate().stripTrailingZeros());
    }
}
