package ru.pozhar.collector_api.service;

import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.collector_api.dto.FileDownloadDTO;
import ru.pozhar.collector_api.exception.EntityNotFoundException;
import ru.pozhar.collector_api.exception.ValidationException;
import ru.pozhar.collector_api.model.Document;
import ru.pozhar.collector_api.model.DocumentFile;
import ru.pozhar.collector_api.openapi.dto.DocumentType;
import ru.pozhar.collector_api.repository.DocumentFileRepository;
import ru.pozhar.collector_api.repository.DocumentRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DocumentFileService {

    private final MinioService minioService;

    private final DocumentFileRepository documentFileRepository;

    private final DocumentRepository documentRepository;

    @Transactional
    public String saveDocumentFile(MultipartFile file, Long debtorId, DocumentType type) {
        validateFile(file);
        Document document = documentRepository.findByDebtorIdAndDocumentType(debtorId, type);
        if (document == null) {
            throw new EntityNotFoundException("Данные документов заемщика не найдены");
        }
        String path = "debtors/" + debtorId + "/documents";
        String fileName = type + ".pdf";
        DocumentFile documentFile = new DocumentFile(
                null,
                document,
                path,
                fileName,
                file.getSize(),
                LocalDate.now()
        );
        documentFileRepository.save(documentFile);
        minioService.uploadFile(file, path, fileName);
        return "Файл " + path + "/" + fileName + " загружен";
    }

    @Transactional(readOnly = true)
    public FileDownloadDTO downloadDocumentFile(Long debtorId, DocumentType type) {
        Document document = documentRepository.findByDebtorIdAndDocumentType(debtorId, type);
        if (document == null) {
            throw new EntityNotFoundException("Данные документов заемщика не найдены");
        }
        DocumentFile documentFile = documentFileRepository.findByDocumentId(document.getId());
        if (documentFile == null) {
            throw new EntityNotFoundException("Данные файла документа не найдены");
        }
        String path = documentFile.getPath();
        String fileName = documentFile.getFileName();
        StatObjectResponse metadata = minioService.downloadMetadata(path, fileName);
        ByteArrayResource resource = minioService.downloadFile(path, fileName);
        return new FileDownloadDTO(resource, fileName, metadata.contentType(), metadata.size());
    }

    private void validateFile(MultipartFile file) {
        if (file == null) {
            throw new ValidationException("Отсутствует файл");
        }
        if (!file.getContentType().equals("application/pdf")) {
            throw new ValidationException("Расширение файла должно быть .pdf");
        }
        if (file.isEmpty()) {
            throw new ValidationException("Файл должен содержать данные");
        }
        if(file.getSize() > 1024L * 1024 * 30) {
            throw new ValidationException("Размер файла должен быть не более 30 MB");
        }
    }
}
