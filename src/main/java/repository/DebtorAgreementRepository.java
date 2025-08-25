package repository;

import model.DebtorAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtorAgreementRepository extends JpaRepository<DebtorAgreement, Long> {
}
