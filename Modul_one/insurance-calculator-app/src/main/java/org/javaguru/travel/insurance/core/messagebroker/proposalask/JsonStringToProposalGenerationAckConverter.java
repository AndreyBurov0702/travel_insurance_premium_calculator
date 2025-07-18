package org.javaguru.travel.insurance.core.messagebroker.proposalask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
class JsonStringToProposalGenerationAckConverter {

    public ProposalGenerationAck convert(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ProposalGenerationAck.class);
    }
}
