package org.javaguru.travel.insurance.core.messagebroker;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PROPOSAL_GENERATION = "q.proposal-generation";

    public static final String QUEUE_PROPOSAL_GENERATION_ACK_DLQ = "q.proposal-generation-ack-dlq";

    public static final String QUEUE_PROPOSAL_GENERATION_ACK = "q.proposal-generation-ack";

    @Bean
    public Queue createProposalPdfGenerationQueue() {
        return new Queue(QUEUE_PROPOSAL_GENERATION);
    }

    @Bean
    public Queue createProposalGenerationAckDlq() {
        return new Queue(QUEUE_PROPOSAL_GENERATION_ACK_DLQ);
    }

    @Bean
    public Queue createProposalGenerationAckQueue() {
        return new Queue(QUEUE_PROPOSAL_GENERATION_ACK);
    }
}
