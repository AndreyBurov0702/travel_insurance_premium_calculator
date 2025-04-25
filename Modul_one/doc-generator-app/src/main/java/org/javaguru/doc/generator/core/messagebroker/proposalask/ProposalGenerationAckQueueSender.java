package org.javaguru.doc.generator.core.messagebroker.proposalask;

import org.javaguru.doc.generator.core.api.dto.AgreementDTO;

public interface ProposalGenerationAckQueueSender {
    void send(AgreementDTO agreement, String proposalFilePath);
}
