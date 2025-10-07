package ru.pozhar.parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.parser.client.CollectorApiClient;
import ru.pozhar.parser.openapi.dto.RequestAgreementDTO;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementsSendService {
    private final CollectorApiClient collectorApiClient;
    private final RabbitSenderService rabbitSenderService;

    public void sendAgreementsList(List<List<RequestAgreementDTO>> batches) {
        List<Long> resultList = new ArrayList<>();
        try {
            for (List<RequestAgreementDTO> agreementDTOs : batches) {
                List<Long> batchList = collectorApiClient.postAgreementsList(agreementDTOs);
                resultList.addAll(batchList);
            }
            rabbitSenderService.sendTransactionComplete(resultList);
        } catch (Exception ex) {
            rabbitSenderService.sendTransactionNotComplete(resultList);
            throw new RuntimeException("Исключение при отправке батчей в сервисе");
        }
    }
}
