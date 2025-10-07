package ru.pozhar.parser.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_NAME = "agreement.transaction.exchange";
    public static final String AGREEMENT_TRANSACTION_QUEUE = "agreement.transaction.queue";
    public static final String AGREEMENT_TRANSACTION_KEY = "agreement.transaction";

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue agreementTransactionQueue() {
        return new Queue(AGREEMENT_TRANSACTION_QUEUE, true);
    }

    @Bean
    public Binding bindingAgreementTransaction(Queue agreementTransactionQueue, TopicExchange transactionExchange) {
        return BindingBuilder.bind(agreementTransactionQueue)
                .to(transactionExchange)
                .with(AGREEMENT_TRANSACTION_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setMandatory(true);
        return template;
    }
}
