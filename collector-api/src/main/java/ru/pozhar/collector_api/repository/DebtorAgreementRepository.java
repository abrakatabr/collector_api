package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DebtorAgreementRepository extends JpaRepository<DebtorAgreement, Long> {
    DebtorAgreement findByDebtorAndAgreement(Debtor debtor, Agreement agreement);

    @Query(value = """
            SELECT * FROM debtors_agreements
            WHERE debtor_id = :debtorId AND agreement_id = :agreementId
            """, nativeQuery = true)
    DebtorAgreement findByDebtorIdAndAgreementId(@Param("debtorId") Long debtorId,
                                                           @Param("agreementId") Long agreementId);

    List<DebtorAgreement> findByAgreement(Agreement agreement);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM DebtorAgreement da WHERE da.agreement.id in :ids
            """)
    void deleteDebtorAgreementsByAgreementId(List<Long> ids);
}
