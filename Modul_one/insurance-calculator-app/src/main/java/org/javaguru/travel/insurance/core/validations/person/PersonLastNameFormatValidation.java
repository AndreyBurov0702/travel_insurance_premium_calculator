package org.javaguru.travel.insurance.core.validations.person;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.util.Placeholder;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PersonLastNameFormatValidation extends TravelPersonFieldValidationImpl{

    private final ValidationErrorFactory errorFactory;

    @Override
    public Optional<ValidationErrorDTO> validate(AgreementDTO agreement, PersonDTO person) {
        if (!isPersonLastNameNullOrBlank(person) && !isValidFormat(person)) {
            Placeholder placeholder = new Placeholder("PERSON_LAST_NAME", person.getPersonLastName());
            return Optional.of(errorFactory.buildError("ERROR_CODE_23", List.of(placeholder)));
        } else {
            return Optional.empty();
        }
    }

    private boolean isPersonLastNameNullOrBlank(PersonDTO person) {
        return person.getPersonLastName() == null || person.getPersonLastName().isBlank();
    }

    private boolean isValidFormat(PersonDTO person) {
        String patternString = "^[A-Za-z]+([ -][A-Za-z]+)*$";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(person.getPersonLastName());
        return matcher.matches();
    }
}

