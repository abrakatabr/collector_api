package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pozhar.collector_api.model.Debtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.Documents;

import java.util.Optional;

@Repository
public interface DebtorRepository extends JpaRepository<Debtor, Long> {
    @Query(value = """
    SELECT d.* FROM debtors d 
    JOIN debtors_documents dd ON d.id = dd.debtor_id 
    WHERE dd.documents_id = :documentId
    """, nativeQuery = true)
    Optional<Debtor> findByDocumentsId(@Param("documentId") Long documentId);
}
