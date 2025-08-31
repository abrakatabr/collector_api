package ru.pozhar.collector_api.repository;

import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebtorAgreementRepository extends JpaRepository<DebtorAgreement, Long> {
    Optional<DebtorAgreement> findByDebtorAndAgreement(Debtor debtor, Agreement agreement);
}
