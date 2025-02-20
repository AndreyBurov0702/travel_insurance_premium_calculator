package org.javaguru.travel.insurance.core.validations;

import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class TravelAgreementUuidValidatorImplTest {

    @Mock
    private ValidationErrorFactory errorFactory;
    @Mock
    private AgreementEntityRepository agreementEntityRepository;
    @InjectMocks
    private TravelAgreementUuidValidatorImpl validator;

    @Test
    public void shouldNotReturnErrorsWhenUuidIsValid() {
        String validUuid = "08060c1b-ddc3-4bac-8fbf-dc9f3554f859";

        when(agreementEntityRepository.findByUuid(validUuid)).thenReturn(Optional.of(new AgreementEntity()));
        List<ValidationErrorDTO> errors = validator.validate(validUuid);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void shouldReturnErrorWhenUuidIsNull() {
        ValidationErrorDTO mockError = new ValidationErrorDTO();
        mockError.setErrorCode("ERROR_CODE_17");
        when(errorFactory.buildError("ERROR_CODE_17")).thenReturn(mockError);

        List<ValidationErrorDTO> errors = validator.validate(null);
        assertEquals(errors.size(), 1);
        assertEquals(errors.get(0).getErrorCode(), "ERROR_CODE_17");
    }

    @Test
    public void shouldReturnErrorWhenUuidNotFoundInRepository() {
        String invalidUuid = "08060c1b-ddc3-4bac-8fbf-dc9f3554f859";
        when(agreementEntityRepository.findByUuid(invalidUuid)).thenReturn(Optional.empty());

        ValidationErrorDTO error = new ValidationErrorDTO();
        error.setErrorCode("ERROR_CODE_18");
        when(errorFactory.buildError(eq("ERROR_CODE_18"), anyList())).thenReturn(error);

        List<ValidationErrorDTO> errors = validator.validate(invalidUuid);
        assertEquals(errors.size(), 1);
        assertEquals(errors.get(0).getErrorCode(), "ERROR_CODE_18");
        verify(errorFactory).buildError(eq("ERROR_CODE_18"), anyList());
    }
}
