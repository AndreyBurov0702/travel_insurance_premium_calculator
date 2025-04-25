package org.javaguru.doc.generator.core.messagebroker.proposal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaguru.doc.generator.core.api.dto.AgreementDTO;
import org.springframework.stereotype.Component;

@Component
class JsonStringToAgreementDtoConverter {

    public AgreementDTO convert(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, AgreementDTO.class);
    }
}
