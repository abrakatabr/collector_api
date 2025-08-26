package ru.pozhar.collector_api.controller;

import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                .header("Location", "api/agreements/" + responseAgreementDTO.id())
                .body(responseAgreementDTO);
    }
}
