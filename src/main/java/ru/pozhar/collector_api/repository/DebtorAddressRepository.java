package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.DebtorAddress;

@Repository
public interface DebtorAddressRepository extends JpaRepository<DebtorAddress, Long> {
}
