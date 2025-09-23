package ru.pozhar.collector_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.openapi.api.DebtorApi;
import ru.pozhar.collector_api.openapi.dto.RequestUpdateDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseGetDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseUpdateDebtorDTO;
import ru.pozhar.collector_api.service.DebtorAgreementService;
import ru.pozhar.collector_api.service.DebtorService;
import ru.pozhar.collector_api.service.NotificationService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DebtorController implements DebtorApi {

    private final DebtorService debtorService;
    private final DebtorAgreementService debtorAgreementService;

    @Override
    public ResponseEntity<ResponseUpdateDebtorDTO> updateDebtor(Long debtorId, RequestUpdateDebtorDTO debtorDTO) {
        ResponseUpdateDebtorDTO responseDebtorDTO = debtorService.updateDebtor(debtorId, debtorDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "/api/debtors/" + responseDebtorDTO.getId())
                .body(responseDebtorDTO);
    }

    @Override
    public ResponseEntity<ResponseGetDebtorDTO> getDebtor(Long debtorId) {
        ResponseGetDebtorDTO response = debtorService.getDebtor(debtorId);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "/api/debtors/" + response.getId())
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteDebtorFromAgreement(Long debtorId, Long agreementId) {
        debtorAgreementService.deleteDebtorFromAgreement(debtorId, agreementId);
        return ResponseEntity.noContent().build();
    }
}
