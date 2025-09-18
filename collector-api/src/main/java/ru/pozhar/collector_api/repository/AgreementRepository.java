package ru.pozhar.collector_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.AgreementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    Page<Agreement> findAllByStatus(AgreementStatus status, Pageable pageable);

    Page<Agreement> findAllByStatusNot(AgreementStatus status, Pageable pageable);
}
