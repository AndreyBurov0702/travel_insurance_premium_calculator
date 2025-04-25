package org.javaguru.blacklist.core.services;

import org.javaguru.blacklist.core.api.BlackListedPersonCoreCommand;
import org.javaguru.blacklist.core.api.BlackListedPersonCoreResult;
import org.javaguru.blacklist.core.api.BlackListedPersonDTO;
import org.javaguru.blacklist.core.api.ValidationErrorDTO;
import org.javaguru.blacklist.core.domain.BlackListedPersonEntity;
import org.javaguru.blacklist.core.repositories.BlackListedPersonEntityRepository;
import org.javaguru.blacklist.core.validations.BlackListedPersonValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlackListedPersonServiceImplTest {

    @Mock
    private BlackListedPersonValidator personValidator;
    @Mock
    private BlackListedPersonEntityRepository repository;

    @InjectMocks
    private BlackListedPersonServiceImpl service;

    @Test
    void calculatePremium_withValidationErrors() {
        BlackListedPersonCoreCommand command = new BlackListedPersonCoreCommand(new BlackListedPersonDTO());
        when(personValidator.validate(any(BlackListedPersonDTO.class)))
                .thenReturn(Collections.singletonList(new ValidationErrorDTO("ERROR_CODE_1",
                        "Field personFirstName must not be empty!")));

        BlackListedPersonCoreResult result = service.check(command);

        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream()
                .anyMatch(e -> "ERROR_CODE_1".equals(e.getErrorCode())));
    }

    @Test
    void calculatePremium_personIsBlacklisted() {
        BlackListedPersonDTO personDTO = new BlackListedPersonDTO();
        personDTO.setPersonFirstName("Vasja");
        personDTO.setPersonLastName("Pupkin");
        personDTO.setPersonCode("123456-12345");

        BlackListedPersonCoreCommand command = new BlackListedPersonCoreCommand(personDTO);
        when(personValidator.validate(any(BlackListedPersonDTO.class))).thenReturn(Collections.emptyList());
        when(repository.findBy("Vasja", "Pupkin", "123456-12345")).thenReturn(Optional.of(new BlackListedPersonEntity()));

        BlackListedPersonCoreResult result = service.check(command);

        assertTrue(result.getPersonDTO().getBlackListed());
    }

    @Test
    void calculatePremium_personIsNotBlacklisted() {
        BlackListedPersonDTO personDTO = new BlackListedPersonDTO();
        personDTO.setPersonFirstName("Vasja");
        personDTO.setPersonLastName("Pupkin");
        personDTO.setPersonCode("123456-1234");

        BlackListedPersonCoreCommand command = new BlackListedPersonCoreCommand(personDTO);
        when(personValidator.validate(any(BlackListedPersonDTO.class))).thenReturn(Collections.emptyList());
        when(repository.findBy("Vasja", "Pupkin", "123456-1234")).thenReturn(Optional.empty());

        BlackListedPersonCoreResult result = service.check(command);

        assertFalse(result.getPersonDTO().getBlackListed());
    }
}