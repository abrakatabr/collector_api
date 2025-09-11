package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.DocumentFile;

@Repository
public interface DocumentFileRepository extends JpaRepository<DocumentFile, Long> {

    DocumentFile findByDocumentId(Long documentId);
}
