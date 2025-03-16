package org.javaguru.travel.insurance.core.repositories.entities;

import org.javaguru.travel.insurance.core.domain.entities.PersonEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PersonEntityRepositoryTest {

    @Autowired
    private PersonEntityRepository repository;

    @Test
    @DisplayName("Test: PersonEntity repository is injected and not null")
    public void injectedRepositoryAreNotNull() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test: Can find PersonEntity by firstName, lastName, personCode and birthDate ")
    public void shouldFindPersonEntityByFullNameAndCode() {
        var firstName = "Vasja";
        var lastName = "Pupkin";
        var personCode = "123456-12345";
        var birthDate = "29.05.2000";

        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName(firstName);
        personEntity.setLastName(lastName);
        personEntity.setPersonCode(personCode);
        personEntity.setBirthDate(createDate(birthDate));
        repository.save(personEntity);

        Optional<PersonEntity> person = repository.findBy(firstName, lastName, personCode);
        assertTrue(person.isPresent());
        assertEquals(person.get().getFirstName(), firstName);
        assertEquals(person.get().getLastName(), lastName);
        assertEquals(person.get().getPersonCode(), personCode);
    }

    @Test
    @DisplayName("Test: Can't find PersonEntity with non-existing data")
    public void shouldNotFindPersonEntityWithFakeData() {
        String firstName = "FakeFirstName";
        String lastName = "FakeLastName";
        String personCode = "FakeCode";

        Optional<PersonEntity> person = repository.findBy(firstName, lastName, personCode);
        assertTrue(person.isEmpty());
    }
    private Date createDate(String dateStr) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
