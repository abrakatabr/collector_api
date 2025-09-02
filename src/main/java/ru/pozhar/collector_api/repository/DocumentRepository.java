package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.*;

import java.util.*;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByDocumentTypeAndDocumentNumber(DocumentType documentType, String documentNumber);

    List<Document> findByDebtor(Debtor debtor);
}
