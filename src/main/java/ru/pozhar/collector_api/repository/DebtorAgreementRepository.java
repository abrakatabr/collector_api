package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtorAgreementRepository extends JpaRepository<DebtorAgreement, Long> {
    Optional<DebtorAgreement> findByDebtorAndAgreement(Debtor debtor, Agreement agreement);

    @Query(value = """
            SELECT * FROM debtors_agreements
            WHERE debtor_id = :debtorId AND agreement_id = :agreementId
            """, nativeQuery = true)
    Optional<DebtorAgreement> findByDebtorIdAndAgreementId(@Param("debtorId") Long debtorId,
                                                           @Param("agreementId") Long agreementId);

    List<DebtorAgreement> findByAgreement(Agreement agreement);
}
