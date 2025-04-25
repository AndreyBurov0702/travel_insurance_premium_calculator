package org.javaguru.travel.insurance.core.messagebroker.proposalask;

import org.javaguru.travel.insurance.core.messagebroker.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("mysql-container")
public class ProposalGenerationAckQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(ProposalGenerationAckQueueListener.class);
    private final Integer totalRetryCount;
    private final JsonStringToProposalGenerationAckConverter proposalGenerationAckConverter;
    private final ProposalGenerationAckService proposalGenerationAckService;
    private final RabbitTemplate rabbitTemplate;

    ProposalGenerationAckQueueListener(@Value("${rabbitmq.total.retry.count:3}")
                                       Integer totalRetryCount,
                                       JsonStringToProposalGenerationAckConverter proposalGenerationAckConverter,
                                       ProposalGenerationAckService proposalGenerationAckService,
                                       RabbitTemplate rabbitTemplate) {
        this.totalRetryCount = totalRetryCount;
        this.proposalGenerationAckConverter = proposalGenerationAckConverter;
        this.proposalGenerationAckService = proposalGenerationAckService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK)
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
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK, message);
        } else {
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK_DLQ, message);
        }
    }

    private void processMessage(Message message) throws Exception {
        String messageBody = new String(message.getBody());
        logger.info(messageBody);
        ProposalGenerationAck proposalGenerationAck = proposalGenerationAckConverter.convert(messageBody);
        proposalGenerationAckService.process(proposalGenerationAck);
    }
}
