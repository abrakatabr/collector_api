package ru.pozhar.camunda.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CollectorApiClient {

    private final RestTemplate restTemplate;

    @Value("${COLLECTOR_API_URL}")
    private String collectorApiUrl;

    public boolean generateNotification(String debtorId, String agreementId) {
        String url = collectorApiUrl + "/debtors/" + debtorId + "/notification/" + agreementId;
        ResponseEntity<ByteArrayResource> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                ByteArrayResource.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return true;
        } else {
            throw new RuntimeException("Ошибка при генерации уведомления");
        }
    }
}
