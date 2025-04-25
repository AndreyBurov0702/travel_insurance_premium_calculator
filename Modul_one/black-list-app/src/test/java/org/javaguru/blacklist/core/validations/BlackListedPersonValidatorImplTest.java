package org.javaguru.blacklist.core.validations;

import org.javaguru.blacklist.core.api.BlackListedPersonDTO;
import org.javaguru.blacklist.core.api.ValidationErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlackListedPersonValidatorImplTest {

    private ValidationErrorFactory errorFactory;
    private BlackListedPersonValidatorImpl validator;

    @BeforeEach
    void setUp() {
        errorFactory = mock(ValidationErrorFactory.class);
        validator = new BlackListedPersonValidatorImpl(errorFactory);
    }

    @Test
    void shouldReturnErrorWhenFirstNameIsEmpty() {
        BlackListedPersonDTO dto = BlackListedPersonDTO.builder()
                .personFirstName("")
                .personLastName("Pupkin")
                .personCode("123456-12345")
                .build();

        ValidationErrorDTO expectedError = new ValidationErrorDTO("ERROR_CODE_1", "Field personFirstName must not be empty!");
        when(errorFactory.buildError("ERROR_CODE_1")).thenReturn(expectedError);

        List<ValidationErrorDTO> errors = validator.validate(dto);

        assertEquals(1, errors.size());
        assertEquals("ERROR_CODE_1", errors.get(0).getErrorCode());
    }

    @Test
    void shouldReturnErrorWhenLastNameIsNull() {
        BlackListedPersonDTO dto = BlackListedPersonDTO.builder()
                .personFirstName("Vasja")
                .personLastName(null)
                .personCode("123456-12345")
                .build();

        ValidationErrorDTO expectedError = new ValidationErrorDTO("ERROR_CODE_2", "Field personLastName must not be empty!");
        when(errorFactory.buildError("ERROR_CODE_2")).thenReturn(expectedError);

        List<ValidationErrorDTO> errors = validator.validate(dto);

        assertEquals(1, errors.size());
        assertEquals("ERROR_CODE_2", errors.get(0).getErrorCode());
    }

    @Test
    void shouldReturnErrorWhenPersonCodeIsEmpty() {
        BlackListedPersonDTO dto = BlackListedPersonDTO.builder()
                .personFirstName("Vasja")
                .personLastName("Pupkin")
                .personCode("")
                .build();

        ValidationErrorDTO expectedError = new ValidationErrorDTO("ERROR_CODE_3", "Field personCode must not be empty!");
        when(errorFactory.buildError("ERROR_CODE_3")).thenReturn(expectedError);

        List<ValidationErrorDTO> errors = validator.validate(dto);

        assertEquals(1, errors.size());
        assertEquals("ERROR_CODE_3", errors.get(0).getErrorCode());
    }

    @Test
    void shouldReturnAllErrorsWhenAllFieldsAreInvalid() {
        BlackListedPersonDTO dto = new BlackListedPersonDTO(null, "", null, null);

        when(errorFactory.buildError("ERROR_CODE_1")).thenReturn(new ValidationErrorDTO("ERROR_CODE_1", "Field personFirstName must not be empty!"));
        when(errorFactory.buildError("ERROR_CODE_2")).thenReturn(new ValidationErrorDTO("ERROR_CODE_2", "Field personLastName must not be empty!"));
        when(errorFactory.buildError("ERROR_CODE_3")).thenReturn(new ValidationErrorDTO("ERROR_CODE_3", "Field personCode must not be empty!"));

        List<ValidationErrorDTO> errors = validator.validate(dto);

        assertEquals(3, errors.size());
    }

    @Test
    void shouldReturnNoErrorsWhenAllFieldsAreValid() {
        BlackListedPersonDTO dto = BlackListedPersonDTO.builder()
                .personFirstName("Vasja")
                .personLastName("Pupkin")
                .personCode("123456-12345")
                .build();

        List<ValidationErrorDTO> errors = validator.validate(dto);

        assertTrue(errors.isEmpty());
        verifyNoInteractions(errorFactory);
    }
}

