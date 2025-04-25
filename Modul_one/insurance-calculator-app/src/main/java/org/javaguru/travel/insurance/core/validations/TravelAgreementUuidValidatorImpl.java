package org.javaguru.travel.insurance.core.validations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementEntityRepository;
import org.javaguru.travel.insurance.core.util.Placeholder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TravelAgreementUuidValidatorImpl implements TravelAgreementUuidValidator{

    private final ValidationErrorFactory errorFactory;
    private final AgreementEntityRepository agreementEntityRepository;

    public List<ValidationErrorDTO> validate(String uuid) {
        List<ValidationErrorDTO> errors = new ArrayList<>();
        if (uuid == null) {
            errors.add(errorFactory.buildError("ERROR_CODE_17"));
        } else {
            if (agreementEntityRepository.findByUuid(uuid).isEmpty()) {
                Placeholder placeholder = new Placeholder("AGREEMENT_UUID", uuid);
                errors.add(errorFactory.buildError("ERROR_CODE_18", List.of(placeholder)));
            }
        }
        return errors;
    }
}
