package ru.pozhar.parser.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.pozhar.parser.openapi.dto.RequestAgreementDTO;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CollectorApiClient {

    private final RestTemplate restTemplate;

    @Value("${COLLECTOR_API_URL}")
    private String collectorApiUrl;

    public List<Long> postAgreementsList(List<RequestAgreementDTO> agreementDTOs) {
        String url = collectorApiUrl + "/agreements/list";
        List<Long> resultList = new ArrayList<>();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<RequestAgreementDTO>> entity = new HttpEntity<>(agreementDTOs, headers);
            ResponseEntity<List<Long>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<Long>>() {}
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                resultList.addAll(response.getBody());
            } else {
                throw new RuntimeException("Исключение при отправке батча в collector-api");
            }
        return resultList;
    }
}
