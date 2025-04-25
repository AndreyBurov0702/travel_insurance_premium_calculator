package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.domain.entities.PersonEntity;
import org.javaguru.travel.insurance.core.repositories.entities.PersonEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PersonEntityFactoryTest {
    @Mock
    private PersonEntityRepository repository;
    @InjectMocks
    private PersonEntityFactory personEntityFactory;

    @Test
    void shouldReturnExistingPersonWhenPersonExists() {
        var personDTO = new PersonDTO();
        personDTO.setPersonFirstName("Vasja");
        personDTO.setPersonLastName("Pupkin");
        personDTO.setPersonCode("123456-12345");

        var existingPerson = new PersonEntity();
        existingPerson.setFirstName("Vasja");
        existingPerson.setLastName("Pupkin");
        existingPerson.setPersonCode("123456-12345");

        when(repository.findBy(personDTO.getPersonFirstName(), personDTO.getPersonLastName(), personDTO.getPersonCode()))
                .thenReturn(Optional.of(existingPerson));
        PersonEntity result = personEntityFactory.createPersonEntity(personDTO);

        assertEquals(existingPerson, result);
        verify(repository, never()).save(any(PersonEntity.class));
    }

    @Test
    void shouldSaveNewPersonWhenPersonDoesNotExist() {
        var personDTO = new PersonDTO();
        personDTO.setPersonFirstName("Vasja");
        personDTO.setPersonLastName("Pupkin");
        personDTO.setPersonCode("123456-12345");

        var savedPerson = new PersonEntity();
        savedPerson.setFirstName(personDTO.getPersonFirstName());
        savedPerson.setLastName(personDTO.getPersonLastName());
        savedPerson.setPersonCode(personDTO.getPersonCode());
        savedPerson.setBirthDate(personDTO.getPersonBirthDate());

        when(repository.findBy(personDTO.getPersonFirstName(), personDTO.getPersonLastName(), personDTO.getPersonCode()))
                .thenReturn(Optional.empty());
        when(repository.save(any(PersonEntity.class))).thenReturn(savedPerson);
        PersonEntity result = personEntityFactory.createPersonEntity(personDTO);

        assertNotNull(result);
        assertEquals(personDTO.getPersonFirstName(), result.getFirstName());
        assertEquals(personDTO.getPersonLastName(), result.getLastName());
        assertEquals(personDTO.getPersonCode(), result.getPersonCode());
        verify(repository).save(any(PersonEntity.class));
    }
}

