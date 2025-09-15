package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pozhar.collector_api.model.Debtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtorRepository extends JpaRepository<Debtor, Long> {
    @Query(value = """
    SELECT d.* FROM debtors d 
    JOIN documents dd ON d.id = dd.debtor_id 
    WHERE dd.documents_id = :documentId
    """, nativeQuery = true)
    Debtor findByDocumentsId(@Param("documentId") Long documentId);

    @Query(value = """
            SELECT * FROM debtors
            WHERE id = :debtorId
            """, nativeQuery = true)
    Debtor findByDebtorId(@Param("debtorId") Long debtorId);
}
