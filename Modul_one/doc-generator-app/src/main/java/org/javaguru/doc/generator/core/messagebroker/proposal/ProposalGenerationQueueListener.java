package org.javaguru.doc.generator.core.messagebroker.proposal;

import org.javaguru.doc.generator.core.api.dto.AgreementDTO;
import org.javaguru.doc.generator.core.messagebroker.RabbitMQConfig;
import org.javaguru.doc.generator.core.messagebroker.proposalask.ProposalGenerationAckQueueSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProposalGenerationQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(ProposalGenerationQueueListener.class);

    private final Integer totalRetryCount;
    private final RabbitTemplate rabbitTemplate;
    private final JsonStringToAgreementDtoConverter agreementDtoConverter;
    private final ProposalGenerator proposalGenerator;

    private final ProposalGenerationAckQueueSender proposalGenerationAckQueueSender;

    public ProposalGenerationQueueListener(@Value("${rabbitmq.total.retry.count:3}")
                                           Integer totalRetryCount,
                                           RabbitTemplate rabbitTemplate,
                                           JsonStringToAgreementDtoConverter agreementDtoConverter,
                                           ProposalGenerator proposalGenerator,
                                           ProposalGenerationAckQueueSender proposalGenerationAckQueueSender) {
        this.totalRetryCount = totalRetryCount;
        this.rabbitTemplate = rabbitTemplate;
        this.agreementDtoConverter = agreementDtoConverter;
        this.proposalGenerator = proposalGenerator;
        this.proposalGenerationAckQueueSender = proposalGenerationAckQueueSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROPOSAL_GENERATION)
    public void receiveMessage(final Message message) throws Exception {
        try {
            processMessage(message);
        } catch (Exception e) {
            logger.error("FAIL to process message: ", e);
            retryOrForwardToDeadLetterQueue(message);
        }
    }

    private void retryOrForwardToDeadLetterQueue(Message message) {
        Integer retryCount = message.getMessageProperties().getHeader("x-retry-count");
        logger.info("MESSAGE DELIVERY TAG "
                + message.getMessageProperties().getDeliveryTag()
                + " RETRY COUNT = " + retryCount);
        if (retryCount == null) {
            retryCount = 0;
        }
        retryCount++;
        if (retryCount <= totalRetryCount) {
            message.getMessageProperties().setHeader("x-retry-count", retryCount);
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION, message);
        } else {
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_DLQ, message);
        }
    }

    private void processMessage(Message message) throws Exception {
        String messageBody = new String(message.getBody());
        logger.info(messageBody);
        AgreementDTO agreementDTO = agreementDtoConverter.convert(messageBody);
        String filePath = proposalGenerator.generateProposalAndStoreToFile(agreementDTO);
        proposalGenerationAckQueueSender.send(agreementDTO, filePath);
    }
}
