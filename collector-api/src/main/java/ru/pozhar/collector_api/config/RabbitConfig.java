package ru.pozhar.collector_api.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_NAME = "notification.exchange";
    public  static final String AGREEMENT_CREATED_QUEUE = "agreement.created.queue";
    public  static final String AGREEMENT_STATUS_UPDATED_QUEUE = "agreement.status.updated.queue";
    public  static final String AGREEMENT_CREATED_KEY = "agreement.created";
    public  static final String AGREEMENT_STATUS_UPDATED_KEY = "agreement.status.updated";

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue agreementCreatedQueue() {
        return new Queue(AGREEMENT_CREATED_QUEUE, true);
    }

    @Bean
    public Queue agreementStatusUpdatedQueue() {
        return new Queue(AGREEMENT_STATUS_UPDATED_QUEUE, true);
    }

    @Bean
    public Binding bindingAgreementCreated(Queue agreementCreatedQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(agreementCreatedQueue)
                .to(notificationExchange)
                .with(AGREEMENT_CREATED_KEY);
    }

    @Bean
    public Binding bindingAgreementStatusUpdated(Queue agreementStatusUpdatedQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(agreementStatusUpdatedQueue)
                .to(notificationExchange)
                .with(AGREEMENT_STATUS_UPDATED_KEY);
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
