package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.DebtorAddress;

import java.util.Optional;

@Repository
public interface DebtorAddressRepository extends JpaRepository<DebtorAddress, Long> {
    Optional<DebtorAddress> findByDebtorIdAndAddressId(Long debtorId, Long addressId);
}
