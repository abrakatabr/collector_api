package ru.pozhar.collector_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.dto.ResponseDocumentDTO;
import ru.pozhar.collector_api.service.DocumentService;

import java.util.List;

@RestController
@RequestMapping("api/debtors/{debtorId}/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<ResponseDocumentDTO>> getDebtorDocuments(
            @PathVariable Long debtorId) {
        List<ResponseDocumentDTO> responseDocumentDTOs = documentService.getDebtorDocuments(debtorId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/"
                        + debtorId + "/document")
                .body(responseDocumentDTOs);
    }
}
