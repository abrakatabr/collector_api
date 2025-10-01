package ru.pozhar.camunda.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.pozhar.camunda.openapi.dto.ResponseAgreementDTO;
import ru.pozhar.camunda.openapi.dto.Role;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitListenerService {
    private final String AGREEMENT_CREATED_QUEUE = "camunda.agreement.created.queue";
    private final RuntimeService runtimeService;

    @RabbitListener(queues = AGREEMENT_CREATED_QUEUE)
    public void receiveCreateAgreementNotification(ResponseAgreementDTO agreementDTO) {
        try {
            log.info("Получено сообщение о создании договора ID: {}", agreementDTO.getId());
            System.out.println(agreementDTO);
            List<String> debtorsId = agreementDTO.getDebtorDTOs()
                    .stream()
                    .filter(dto -> dto.getRole() == Role.SINGLE_DEBTOR || dto.getRole() == Role.CO_DEBTOR)
                    .map(dto -> String.valueOf(dto.getId()))
                    .collect(Collectors.toList());
            try {
                ProcessInstance processInstance = runtimeService.createProcessInstanceByKey("NotificationProcess")
                        .businessKey("NOTIFICATION_" + agreementDTO.getId())
                        .setVariable("debtorsId", debtorsId)
                        .setVariable("agreementId", String.valueOf(agreementDTO.getId()))
                        .execute();
            } catch (Exception e) {
                log.error("Ошибка при создании процесса Camunda: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке уведомления о создании договора: {}", e.getMessage(), e);
        }
    }
}