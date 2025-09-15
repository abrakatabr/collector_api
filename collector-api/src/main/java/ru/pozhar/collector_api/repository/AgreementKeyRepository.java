package ru.pozhar.collector_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.AgreementKey;

@Repository
public interface AgreementKeyRepository extends JpaRepository<AgreementKey, Long> {
    AgreementKey findByKey(Long key);
}
