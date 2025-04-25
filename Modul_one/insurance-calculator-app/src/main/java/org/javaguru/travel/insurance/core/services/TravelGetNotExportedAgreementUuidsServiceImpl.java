package org.javaguru.travel.insurance.core.services;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelGetNotExportedAgreementUuidsCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetNotExportedAgreementUuidsCoreResult;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TravelGetNotExportedAgreementUuidsServiceImpl implements TravelGetNotExportedAgreementUuidsService {

    private final AgreementEntityRepository agreementRepository;

    @Override
    public TravelGetNotExportedAgreementUuidsCoreResult getAgreementUuids(TravelGetNotExportedAgreementUuidsCoreCommand command) {
        List<String> agreementUuids = agreementRepository.getNotExportedAgreementUuids();
        return new TravelGetNotExportedAgreementUuidsCoreResult(null, agreementUuids);
    }
}
