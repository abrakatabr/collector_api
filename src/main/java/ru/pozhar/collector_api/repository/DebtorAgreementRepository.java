package ru.pozhar.collector_api.repository;

import ru.pozhar.collector_api.model.DebtorAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtorAgreementRepository extends JpaRepository<DebtorAgreement, Long> {
}
