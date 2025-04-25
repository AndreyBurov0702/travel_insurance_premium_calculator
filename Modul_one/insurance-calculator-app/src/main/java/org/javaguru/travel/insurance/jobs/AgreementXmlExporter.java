package org.javaguru.travel.insurance.jobs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelExportAgreementToXmlCoreCommand;
import org.javaguru.travel.insurance.core.services.TravelExportAgreementToXmlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AgreementXmlExporter {

    private static final Logger logger = LoggerFactory.getLogger(AgreementXmlExporterJob.class);

    private final TravelExportAgreementToXmlService agreementToXmlService;

    public void exportAgreement(String agreementUuid) {
        logger.info("AgreementXmlExporterJob started for uuid = " + agreementUuid);
        agreementToXmlService.export(new TravelExportAgreementToXmlCoreCommand(agreementUuid));
        logger.info("AgreementXmlExporterJob finished for uuid = " + agreementUuid);
    }
}
