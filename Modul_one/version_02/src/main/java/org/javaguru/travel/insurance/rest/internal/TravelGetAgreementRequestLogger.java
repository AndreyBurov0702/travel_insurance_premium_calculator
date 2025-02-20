package org.javaguru.travel.insurance.rest.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaguru.travel.insurance.dto.v2.TravelCalculatePremiumRequestV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class TravelGetAgreementRequestLogger {

    private static final Logger logger = LoggerFactory.getLogger(TravelGetAgreementRequestLogger.class);

    void log(String uuid) {
        logger.info("REQUEST: agreement uuid = " + uuid);
    }
}
