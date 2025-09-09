package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.NotificationData;

import java.util.Optional;

@Repository
public interface NotificationDataRepository extends JpaRepository<Debtor, Long> {
    @Query("""
           SELECT d FROM Debtor d WHERE d.id = :debtorId
           """)
    Debtor findDebtorById(@Param("debtorId") Long debtorId);

    @Query("""
           SELECT a FROM Address a 
           WHERE a.debtor.id = :debtorId AND a.addressStatus = 'registration'
           """)
    Optional<Address> findRegistrationAddress(@Param("debtorId") Long debtorId);

    @Query("""
           SELECT agr FROM Agreement agr 
           JOIN DebtorAgreement da ON da.agreement = agr 
           WHERE da.debtor.id = :debtorId AND agr.id = :agreementId
           """)
    Optional<Agreement> findAgreement(@Param("debtorId") Long debtorId,
                                      @Param("agreementId") Long agreementId);

    default NotificationData findByDebtorIdAndAgreementId(Long debtorId, Long agreementId) {
        Debtor debtor = findDebtorById(debtorId);
        Address address = findRegistrationAddress(debtorId).orElse(null);
        Agreement agreement = findAgreement(debtorId, agreementId).orElse(null);

        return new NotificationData(debtor, address, agreement);
    }

}
