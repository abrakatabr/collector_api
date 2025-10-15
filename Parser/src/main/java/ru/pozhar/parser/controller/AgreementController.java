package ru.pozhar.parser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.parser.openapi.dto.RequestAgreementDTO;
import ru.pozhar.parser.service.AgreementsSendService;
import ru.pozhar.parser.service.TableParser;
import ru.pozhar.parser.service.TableParserFactory;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AgreementController {
    private final TableParserFactory tableParserFactory;
    private final AgreementsSendService agreementsSendService;

    @PostMapping(value = "/agreements")
    public ResponseEntity<Void> uploadAgreements(
            @RequestParam("file") MultipartFile file) {
        TableParser parser = tableParserFactory.getTableParser(file.getOriginalFilename().toLowerCase());
        List<List<RequestAgreementDTO>> batches = parser.getBatches(file);
        agreementsSendService.sendAgreementsList(batches);
        return ResponseEntity.noContent().build();
    }
}
