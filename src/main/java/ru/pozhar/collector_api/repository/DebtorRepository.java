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
            SELECT debtor_id FROM debtors_documents
            WHERE documents_id = :documents_id
            """, nativeQuery = true)
    Optional<Debtor> findByDocuments(@Param("documents_id") Documents documents);
}
