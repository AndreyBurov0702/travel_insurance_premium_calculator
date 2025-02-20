package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
import org.javaguru.travel.insurance.core.domain.entities.*;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementEntityRepository;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementPersonEntityRepository;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementPersonRiskEntityRepository;
import org.javaguru.travel.insurance.core.repositories.entities.SelectedRiskEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgreementEntityFactoryTest {

    @Mock
    private AgreementEntityRepository agreementEntityRepository;
    @Mock
    private PersonEntityFactory personEntityFactory;
    @Mock
    private SelectedRiskEntityRepository selectedRiskEntityRepository;
    @Mock
    private AgreementPersonEntityRepository agreementPersonEntityRepository;
    @Mock
    private AgreementPersonRiskEntityRepository agreementPersonRiskEntityRepository;
    @InjectMocks
    private AgreementEntityFactory agreementEntityFactory;

    @Test
    void shouldCreateAndSaveAgreementEntity() {
        // Создание AgreementDTO
        var agreementDTO = new AgreementDTO();
        agreementDTO.setAgreementDateFrom(new Date());
        agreementDTO.setAgreementDateTo(new Date());
        agreementDTO.setCountry("JAPAN");
        agreementDTO.setAgreementPremium(BigDecimal.valueOf(1000));

        // Создание PersonDTO
        var personDTO1 = new PersonDTO();
        personDTO1.setPersonFirstName("Vasja");
        personDTO1.setPersonLastName("Pupkin");
        personDTO1.setPersonCode("123456-12345");

        // Инициализация списка рисков для человека
        var riskDTO = new RiskDTO();
        riskDTO.setRiskIc("TRAVEL_MEDICAL");
        personDTO1.setRisks(Collections.singletonList(riskDTO)); // Инициализация рисков

        // Добавление людей в AgreementDTO
        agreementDTO.setPersons(Collections.singletonList(personDTO1));

        // Добавление выбранных рисков
        var selectedRisks = Arrays.asList("TRAVEL_MEDICAL", "TRAVEL_EVACUATION");
        agreementDTO.setSelectedRisks(selectedRisks);

        // Создание объекта AgreementEntity, который будет сохранен
        var savedAgreementEntity = new AgreementEntity();
        savedAgreementEntity.setAgreementDateFrom(agreementDTO.getAgreementDateFrom());
        savedAgreementEntity.setAgreementDateTo(agreementDTO.getAgreementDateTo());
        savedAgreementEntity.setCountry(agreementDTO.getCountry());
        savedAgreementEntity.setAgreementPremium(agreementDTO.getAgreementPremium());

        // Мокаем методы репозиториев и фабрик
        when(agreementEntityRepository.save(any(AgreementEntity.class))).thenReturn(savedAgreementEntity);
        when(selectedRiskEntityRepository.save(any(SelectedRiskEntity.class))).thenReturn(new SelectedRiskEntity());

        // Мокаем создание PersonEntity
        PersonEntity personEntity = new PersonEntity();
        when(personEntityFactory.createPersonEntity(any(PersonDTO.class))).thenReturn(personEntity);

        // Мокаем сохранение AgreementPersonEntity
        when(agreementPersonEntityRepository.save(any(AgreementPersonEntity.class))).thenReturn(new AgreementPersonEntity());

        // Мокаем сохранение AgreementPersonRiskEntity
        when(agreementPersonRiskEntityRepository.save(any(AgreementPersonRiskEntity.class)))
                .thenReturn(new AgreementPersonRiskEntity());

        // Выполнение тестируемого метода
        AgreementEntity result = agreementEntityFactory.createAgreementEntity(agreementDTO);

        // Проверки
        assertNotNull(result);
        assertEquals(agreementDTO.getCountry(), result.getCountry());
        assertEquals(agreementDTO.getAgreementDateFrom(), result.getAgreementDateFrom());
        assertEquals(agreementDTO.getAgreementDateTo(), result.getAgreementDateTo());
        assertEquals(agreementDTO.getAgreementPremium(), result.getAgreementPremium());

        // Проверка взаимодействий с моками
        verify(personEntityFactory, times(1)).createPersonEntity(personDTO1);
        verify(agreementEntityRepository, times(1)).save(any(AgreementEntity.class));
        verify(selectedRiskEntityRepository, times(2)).save(any(SelectedRiskEntity.class));
        verify(agreementPersonEntityRepository, times(1)).save(any(AgreementPersonEntity.class));
        verify(agreementPersonRiskEntityRepository, times(1)).save(any(AgreementPersonRiskEntity.class));
    }
}
