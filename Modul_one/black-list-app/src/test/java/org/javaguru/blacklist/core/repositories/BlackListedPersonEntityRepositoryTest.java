package org.javaguru.blacklist.core.repositories;

import org.javaguru.blacklist.core.domain.BlackListedPersonEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BlackListedPersonEntityRepositoryTest {

    @Autowired
    private BlackListedPersonEntityRepository repository;

    @Test
    @DisplayName("Test: BlackListedPersonEntity repository is injected and not null")
    public void injectedRepositoryIsNotNull() {
        assertNotNull(repository);
    }

    @Test
    @DisplayName("Test: Can find BlackListedPersonEntity by firstName, lastName, and personCode")
    public void shouldFindBlackListedPersonEntityByFullNameAndCode() {
        String firstName = "Vasja";
        String lastName = "Pupkin";
        String personCode = "123456-12345";

        BlackListedPersonEntity person = new BlackListedPersonEntity();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setPersonCode(personCode);
        repository.save(person);

        Optional<BlackListedPersonEntity> foundPerson = repository.findBy(firstName, lastName, personCode);

        assertTrue(foundPerson.isPresent());
        assertEquals(firstName, foundPerson.get().getFirstName());
        assertEquals(lastName, foundPerson.get().getLastName());
        assertEquals(personCode, foundPerson.get().getPersonCode());
    }

    @Test
    @DisplayName("Test: Can't find BlackListedPersonEntity with non-existing data")
    public void shouldNotFindWithFakeData() {
        String firstName = "FakeFirstName";
        String lastName = "FakeLastName";
        String personCode = "FakeCode0000";

        Optional<BlackListedPersonEntity> person = repository.findBy(firstName, lastName, personCode);
        assertTrue(person.isEmpty());
    }
}
