package ru.pozhar.collector_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.service.DebtorAgreementService;

@RestController
@RequestMapping("/api/debtors/{debtorId}/{agreementId}")
@RequiredArgsConstructor
public class DebtorAgreementController {

    private final DebtorAgreementService debtorAgreementService;

    @DeleteMapping
    public ResponseEntity<Void> deleteDebtorFromAgreement(
            @PathVariable Long debtorId,
            @PathVariable Long agreementId) {
        debtorAgreementService.deleteDebtorFromAgreement(debtorId, agreementId);
        return ResponseEntity.noContent().build();
    }
}
