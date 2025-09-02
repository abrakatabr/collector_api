package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.Document;
import ru.pozhar.collector_api.model.DocumentType;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    List<Document> findByDebtor(Debtor debtor);
}
