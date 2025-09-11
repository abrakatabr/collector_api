package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.collector_api.exception.EntityNotFoundException;
import ru.pozhar.collector_api.exception.ValidationException;
import ru.pozhar.collector_api.model.Document;
import ru.pozhar.collector_api.model.DocumentFile;
import ru.pozhar.collector_api.model.DocumentType;
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
    public String saveDocumentFile(MultipartFile file, Long debtorId, String type) {
        validateFile(file);
        DocumentType documentType = DocumentType.valueOf(type.replace("-", "_").toUpperCase());
        Document document = documentRepository.findByDebtorIdAndDocumentType(debtorId, documentType);
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
