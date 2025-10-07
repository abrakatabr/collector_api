package ru.pozhar.parser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.pozhar.parser.config.RabbitConfig;
import ru.pozhar.parser.openapi.dto.TransactionInfoDTO;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitSenderService {
    private final RabbitTemplate rabbitTemplate;

    public void sendTransactionComplete(List<Long> ids) {
        TransactionInfoDTO transactionInfo = new TransactionInfoDTO();
        transactionInfo.setIds(ids);
        transactionInfo.setIsComplete(true);
        sendDTO(transactionInfo, RabbitConfig.EXCHANGE_NAME, RabbitConfig.AGREEMENT_TRANSACTION_KEY);
    }

    public void sendTransactionNotComplete(List<Long> ids) {
        TransactionInfoDTO transactionInfo = new TransactionInfoDTO();
        transactionInfo.setIds(ids);
        transactionInfo.setIsComplete(false);
        sendDTO(transactionInfo, RabbitConfig.EXCHANGE_NAME, RabbitConfig.AGREEMENT_TRANSACTION_KEY);
    }

    private <TransactionInfoDTO> void sendDTO(TransactionInfoDTO dto, String exchange, String key) {
        try {
            rabbitTemplate.convertAndSend(exchange, key, dto);
        } catch(Exception ex) {
            log.error("Ошибка при отправке данных в RabbitMQ: {}", ex.getMessage(), ex);
        }
    }
}
