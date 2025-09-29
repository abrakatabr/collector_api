package ru.pozhar.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.pozhar.notification.openapi.dto.ResponseAgreementDTO;
import ru.pozhar.notification.openapi.dto.UpdateAgreementNotification;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitListenerService {
    private final String AGREEMENT_CREATED_QUEUE = "agreement.created.queue";
    private final String AGREEMENT_STATUS_UPDATED_QUEUE = "agreement.status.updated.queue";

    @RabbitListener(queues = AGREEMENT_CREATED_QUEUE)
    public void receiveCreateAgreementNotification(ResponseAgreementDTO agreementDTO) {
        try {
            log.info("Получено сообщение о создании договора ID: {}", agreementDTO.getId());
            System.out.println(agreementDTO);
        } catch (Exception e) {
            log.error("Ошибка при обработке уведомления о создании договора: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = AGREEMENT_STATUS_UPDATED_QUEUE)
    public void receiveUpdateAgreementStatusNotification(UpdateAgreementNotification agreementDTO) {
        try {
            log.info("Получено сообщение об изменении статуса договора ID: {} ({} -> {})",
                    agreementDTO.getId(), agreementDTO.getOldStatus(), agreementDTO.getNewStatus());
            System.out.println(agreementDTO);
        } catch (Exception e) {
            log.error("Ошибка при обработке уведомления об изменении статуса договора: {}", e.getMessage(), e);
        }
    }
}
