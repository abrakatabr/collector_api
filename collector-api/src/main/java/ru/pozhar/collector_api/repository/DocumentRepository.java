package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.Document;
import ru.pozhar.collector_api.model.DocumentType;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document findByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    List<Document> findByDebtorId(Long debtorId);

    Document findByDebtorIdAndDocumentType(Long debtorId, DocumentType documentType);
}
