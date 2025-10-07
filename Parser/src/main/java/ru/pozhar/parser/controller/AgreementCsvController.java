package ru.pozhar.parser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.parser.client.CollectorApiClient;
import ru.pozhar.parser.openapi.dto.RequestAgreementDTO;
import ru.pozhar.parser.service.AgreementsSendService;
import ru.pozhar.parser.service.ParseCsvService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AgreementCsvController {
    private final ParseCsvService parseCsvService;
    private final AgreementsSendService agreementsSendService;

    @PostMapping(value = "/agreements")
    public ResponseEntity<Void> uploadAgreementsCSV(
            @RequestParam("file") MultipartFile file) {
        List<List<RequestAgreementDTO>> batches = parseCsvService.parseCsv(file);
        agreementsSendService.sendAgreementsList(batches);
        return ResponseEntity.noContent().build();
    }
}
