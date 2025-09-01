package ru.pozhar.collector_api.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.pozhar.collector_api.dto.ResponseUpdateStatusDTO;
import ru.pozhar.collector_api.exception.ValidationException;
import ru.pozhar.collector_api.model.AgreementStatus;
import ru.pozhar.collector_api.service.AgreementService;

@RestController
@RequestMapping("/api/agreements")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @PostMapping
    public ResponseEntity<ResponseAgreementDTO> createAgreement(
            @RequestBody RequestAgreementDTO requestAgreementDTO) {
        ResponseAgreementDTO responseAgreementDTO = agreementService.createAgreement(requestAgreementDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "api/agreements/create/" + responseAgreementDTO.id())
                .body(responseAgreementDTO);
    }

    @DeleteMapping("/{agreementId}")
    public ResponseEntity<Void> deleteAgreement(@PathVariable Long agreementId) {
        agreementService.deleteAgreement(agreementId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{agreementId}/status")
    public ResponseEntity<ResponseUpdateStatusDTO> updateAgreementStatus(
            @PathVariable Long agreementId,
            @RequestParam String status) {
        if (!StringUtils.hasText(status) || "deleted".equals(status)) {
            throw new ValidationException("Статус должен содержать символы и может быть 'active' или 'paid'");
        }
        AgreementStatus agreementStatus = AgreementStatus.valueOf(status);
        ResponseUpdateStatusDTO updateStatusDTO = agreementService.updateAgreementStatus(agreementId, agreementStatus);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "/api/agreements" + updateStatusDTO.agreementId())
                .body(updateStatusDTO);
    }
}
