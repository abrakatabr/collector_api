package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.openapi.dto.TransactionInfoDTO;
import ru.pozhar.collector_api.repository.AgreementRepository;
import ru.pozhar.collector_api.repository.DebtorAgreementRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitListenerService {
    private final String AGREEMENT_TRANSACTION_QUEUE = "agreement.transaction.queue";
    private final AgreementRepository agreementRepository;
    private final DebtorAgreementRepository debtorAgreementRepository;

    @Transactional
    @RabbitListener(queues = AGREEMENT_TRANSACTION_QUEUE)
    public void receiveAgreementTransaction(TransactionInfoDTO transactionInfo) {
        try {
            if (transactionInfo.getIsComplete()) {
                agreementRepository.transactionCompletedAgreements(transactionInfo.getIds());
            }
            if (!transactionInfo.getIsComplete()) {
                debtorAgreementRepository.deleteDebtorAgreementsByAgreementId(transactionInfo.getIds());
                agreementRepository.transactionNotCompletedAgreements(transactionInfo.getIds());
            }
        } catch (Exception ex) {
            log.error("Ошибка при обработке сообщения о завершении транзакции: {}", ex.getMessage(), ex);
        }
    }
}
