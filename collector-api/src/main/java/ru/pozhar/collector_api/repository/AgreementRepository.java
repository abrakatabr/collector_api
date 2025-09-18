package ru.pozhar.collector_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pozhar.collector_api.model.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.model.AgreementStatus;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    String SEARCH_PAGES_BY_FILTER_QUERY = """
            SELECT a FROM Agreement a
            WHERE (:transferor IS NULL OR :transferor = a.transferor) AND
            (:status IS NULL OR :status = a.status)
            """;

    @Query(value = SEARCH_PAGES_BY_FILTER_QUERY)
    Page<Agreement> findAllAgreements(@Param("transferor") String transferor,
                                      @Param("status") AgreementStatus status,
                                      Pageable pageable);
}
