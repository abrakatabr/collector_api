package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.config.RabbitConfig;
import ru.pozhar.collector_api.openapi.dto.ResponseAgreementDTO;
import ru.pozhar.collector_api.openapi.dto.UpdateAgreementNotification;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitSenderService {
    private final RabbitTemplate rabbitTemplate;

    public void sendAgreementCreatedNotification(ResponseAgreementDTO agreementDTO) {
        sendDTO(agreementDTO, RabbitConfig.EXCHANGE_NAME, RabbitConfig.AGREEMENT_CREATED_KEY);
    }

    public void sendCamundaAgreementCreatedNotification(ResponseAgreementDTO agreementDTO) {
        sendDTO(agreementDTO, RabbitConfig.EXCHANGE_NAME, RabbitConfig.CAMUNDA_AGREEMENT_CREATED_KEY);
    }

    public void sendAgreementUpdatedNotification(UpdateAgreementNotification agreementDTO) {
        sendDTO(agreementDTO, RabbitConfig.EXCHANGE_NAME, RabbitConfig.AGREEMENT_STATUS_UPDATED_KEY);
    }

    private <T> void sendDTO(T dto, String exchange, String key) {
        try {
            rabbitTemplate.convertAndSend(exchange, key, dto);
        } catch(Exception ex) {
            log.error("Ошибка при отправке данных в RabbitMQ: {}", ex.getMessage(), ex);
        }
    }
}
