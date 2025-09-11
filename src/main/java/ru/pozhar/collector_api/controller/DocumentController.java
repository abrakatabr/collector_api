package ru.pozhar.collector_api.controller;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.collector_api.dto.ResponseDocumentDTO;
import ru.pozhar.collector_api.service.DocumentFileService;
import ru.pozhar.collector_api.service.DocumentService;

import java.util.List;

@RestController
@RequestMapping("api/debtors/{debtorId}/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    private final DocumentFileService documentFileService;

    @GetMapping
    public ResponseEntity<List<ResponseDocumentDTO>> getDebtorDocuments(
            @PathVariable Long debtorId) {
        List<ResponseDocumentDTO> responseDocumentDTOs = documentService.getDebtorDocuments(debtorId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/"
                        + debtorId + "/document")
                .body(responseDocumentDTOs);
    }

    @PostMapping(value = "/file", params = "type")
    public ResponseEntity<String> uploadDocumentFile(
            @PathVariable Long debtorId,
            @RequestParam("file") MultipartFile file,
            @Pattern(regexp = "national-passport|international-passport|driver-license|inn|snils",
            message = "Неверный тип документа в запросе")
            @RequestParam("type") String type) {
        String fullPath = documentFileService.saveDocumentFile(file, debtorId, type);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "api/debtors/"
                        + debtorId + "/document/file")
                .body(fullPath);
    }
}
