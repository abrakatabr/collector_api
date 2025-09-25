package ru.pozhar.collector_api.controller;

import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.openapi.api.AgreementApi;
import ru.pozhar.collector_api.openapi.dto.AgreementStatus;
import ru.pozhar.collector_api.openapi.dto.ResponseUpdateStatusDTO;
import ru.pozhar.collector_api.openapi.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseAgreementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.pozhar.collector_api.openapi.dto.ResponsePageAgreement;
import ru.pozhar.collector_api.exception.ValidationException;
import ru.pozhar.collector_api.service.AgreementService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AgreementController implements AgreementApi {

    private final AgreementService agreementService;

    @Override
    public ResponseEntity<ResponsePageAgreement> getAllAgreements(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection,
            String transferor,
            AgreementStatus status) {
        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        ResponsePageAgreement response = agreementService.getAllAgreements(pageable, transferor, status);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "api/agreements")
                .body(response);
    }

    @Override
    public ResponseEntity<ResponseAgreementDTO> createAgreement(
            String key,
            RequestAgreementDTO requestAgreementDTO) {
        Long agreementKey = validateKey(key);
        ResponseAgreementDTO responseAgreementDTO = agreementService.createAgreement(requestAgreementDTO, agreementKey);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "api/agreements" + responseAgreementDTO.getId())
                .body(responseAgreementDTO);
    }

    @Override
    public ResponseEntity<Void> deleteAgreement(Long agreementId) {
        agreementService.deleteAgreement(agreementId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ResponseUpdateStatusDTO> updateAgreementStatus(Long agreementId, AgreementStatus status) {
        if (status == null
                || (status != AgreementStatus.PAID && status != AgreementStatus.ACTIVE)) {
            throw new ValidationException("Статус должен содержать символы и может быть 'active' или 'paid'");
        }
        ResponseUpdateStatusDTO updateStatusDTO = agreementService.updateAgreementStatus(agreementId, status);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "/api/agreements/"
                        + updateStatusDTO.getAgreementId() + "status")
                .body(updateStatusDTO);
    }

    @Override
    public ResponseEntity<ResponseAgreementDTO> getAgreement(Long agreementId) {
        ResponseAgreementDTO response = agreementService.getAgreement(agreementId);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "/api/agreements/"
                        + response.getId())
                .body(response);
    }

    private Long validateKey(String key) {
        if (!StringUtils.hasText(key)) {
            throw new ValidationException("Ключ идемпотентности должен содержать символы");
        }
        Long agreementKey;
        try {
            agreementKey = Long.valueOf(key);
        } catch (NumberFormatException exception) {
            throw new ValidationException("Ключ идемпотентности должен быть числом");
        }
        return agreementKey;
    }
}
