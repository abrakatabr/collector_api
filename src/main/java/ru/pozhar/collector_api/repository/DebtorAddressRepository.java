package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.DebtorAddress;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtorAddressRepository extends JpaRepository<DebtorAddress, Long> {
    Optional<DebtorAddress> findByDebtorIdAndAddressId(Long debtorId, Long addressId);
    @Query(value = """
            SELECT address_id FROM debtors_addresses
            WHERE debtor_id = :debtorId
            """, nativeQuery = true)
    List<Long> findAddressesIdByDebtorId(@Param("debtorId") Long debtorId);

    Optional<DebtorAddress> findDebtorAddressByDebtorIdAndAddressId(Long debtorId, Long addressId);
}
