package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.AgreementKey;

import java.util.Optional;

@Repository
public interface AgreementKeyRepository extends JpaRepository<AgreementKey, Long> {
    Optional<AgreementKey> findByKey(Long key);
}
