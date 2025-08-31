package ru.pozhar.collector_api.repository;

import org.springframework.data.domain.Example;
import ru.pozhar.collector_api.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByDebtorId(Long debtorId);
}
