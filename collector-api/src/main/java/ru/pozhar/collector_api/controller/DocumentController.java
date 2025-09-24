package ru.pozhar.collector_api.controller;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.collector_api.dto.FileDownloadDTO;
import ru.pozhar.collector_api.openapi.api.DocumentApi;
import ru.pozhar.collector_api.openapi.dto.ResponseDocumentDTO;
import ru.pozhar.collector_api.service.DocumentFileService;
import ru.pozhar.collector_api.service.DocumentService;
import ru.pozhar.collector_api.service.NotificationService;
import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class DocumentController implements DocumentApi {

    private final DocumentService documentService;

    private final DocumentFileService documentFileService;

    private final NotificationService notificationService;

    @Override
    public ResponseEntity<List<ResponseDocumentDTO>> getDebtorDocuments(Long debtorId) {
        List<ResponseDocumentDTO> responseDocumentDTOs = documentService.getDebtorDocuments(debtorId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/"
                        + debtorId + "/document")
                .body(responseDocumentDTOs);
    }

    @Override
    public ResponseEntity<String> uploadDocumentFile(
            Long debtorId,
            String type,
            MultipartFile file) {
        String fullPath = documentFileService.saveDocumentFile(file, debtorId, type);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "api/debtors/"
                        + debtorId + "/document/file")
                .body(fullPath);
    }

    @Override
    public ResponseEntity<Resource> downloadDocumentFile(
            Long debtorId,
            String type) {
        FileDownloadDTO file = documentFileService.downloadDocumentFile(debtorId, type);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, file.contentType())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.size()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.filename() + "\"")
                .body(file.resource());
    }

    @Override
    public ResponseEntity<Resource> getNotification(Long debtorId, Long agreementId) {
        ByteArrayResource response = notificationService.getNotification(debtorId, agreementId);
        String filename = "notification_" + debtorId + "_" + agreementId + ".txt";
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(response.getByteArray().length))
                .header("Location", "/api/debtors/"
                        + debtorId + "/notification/" + agreementId)
                .body(response);
    }
}
