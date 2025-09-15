package ru.pozhar.collector_api.repository;

import ru.pozhar.collector_api.model.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}
