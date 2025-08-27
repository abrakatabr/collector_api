package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.DebtorDocuments;

import java.util.Optional;

@Repository
public interface DebtorDocumentsRepository extends JpaRepository<DebtorDocuments, Long> {
    Optional<DebtorDocuments> findByDebtorId(Long debtorId);
}
