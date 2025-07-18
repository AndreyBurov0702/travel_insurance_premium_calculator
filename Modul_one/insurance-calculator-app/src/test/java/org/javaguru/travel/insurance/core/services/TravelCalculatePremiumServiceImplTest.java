package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreResult;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.messagebroker.proposal.ProposalGeneratorQueueSender;
import org.javaguru.travel.insurance.core.validations.TravelAgreementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TravelCalculatePremiumServiceImplTest {

    @Mock
    private TravelAgreementValidator agreementValidator;
    @Mock
    private AgreementPersonsPremiumCalculator agreementPersonsPremiumCalculator;
    @Mock
    private AgreementTotalPremiumCalculator agreementTotalPremiumCalculator;
    @Mock
    private AgreementEntityFactory agreementEntityFactory;
    @Mock
    private ProposalGeneratorQueueSender proposalGeneratorQueueSender;

    @InjectMocks
    private TravelCalculatePremiumServiceImpl service;

    @Test
    public void shouldReturnValidationErrors() {
        var agreement = new AgreementDTO();
        var command = new TravelCalculatePremiumCoreCommand(agreement);

        var validationError = new ValidationErrorDTO("Error code", "Error description");
        when(agreementValidator.validate(agreement)).thenReturn(List.of(validationError));

        TravelCalculatePremiumCoreResult result = service.calculatePremium(command);

        assertEquals(result.getErrors().size(), 1);
        assertEquals(result.getErrors().get(0).getErrorCode(), "Error code");
        assertEquals(result.getErrors().get(0).getDescription(), "Error description");
        verifyNoInteractions(agreementPersonsPremiumCalculator, agreementEntityFactory, proposalGeneratorQueueSender);
    }

    @Test
    public void shouldCalculatePersonsPremium() {
        var person = new PersonDTO();
        var agreement = new AgreementDTO();
        agreement.setPersons(List.of(person));
        when(agreementValidator.validate(agreement)).thenReturn(Collections.emptyList());
        var agreementEntity = new AgreementEntity();
        when(agreementEntityFactory.createAgreementEntity(agreement)).thenReturn(agreementEntity);

        service.calculatePremium(new TravelCalculatePremiumCoreCommand(agreement));

        verify(agreementPersonsPremiumCalculator).calculateRiskPremiums(agreement);
        verify(proposalGeneratorQueueSender).send(agreement);
    }


    @Test
    public void shouldSaveAgreement() {
        var person = new PersonDTO();
        var agreement = new AgreementDTO();
        agreement.setPersons(List.of(person));
        when(agreementValidator.validate(agreement)).thenReturn(Collections.emptyList());
        var agreementEntity = new AgreementEntity();
        when(agreementEntityFactory.createAgreementEntity(agreement)).thenReturn(agreementEntity);

        service.calculatePremium(new TravelCalculatePremiumCoreCommand(agreement));

        verify(agreementEntityFactory).createAgreementEntity(agreement);
        verify(proposalGeneratorQueueSender).send(agreement);
    }

    @Test
    public void shouldCalculateAgreementTotalPremium() {
        var person = new PersonDTO();
        var agreement = new AgreementDTO();
        agreement.setPersons(List.of(person));
        when(agreementValidator.validate(agreement)).thenReturn(Collections.emptyList());
        var agreementEntity = new AgreementEntity();
        when(agreementEntityFactory.createAgreementEntity(agreement)).thenReturn(agreementEntity);
        when(agreementTotalPremiumCalculator.calculate(agreement)).thenReturn(BigDecimal.ONE);

        TravelCalculatePremiumCoreResult result = service.calculatePremium(new TravelCalculatePremiumCoreCommand(agreement));

        assertEquals(result.getAgreement().getAgreementPremium(), BigDecimal.ONE);
        verify(proposalGeneratorQueueSender).send(agreement);
    }
}



