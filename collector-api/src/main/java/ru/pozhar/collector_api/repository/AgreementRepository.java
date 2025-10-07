package ru.pozhar.collector_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.model.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pozhar.collector_api.openapi.dto.AgreementStatus;

import java.util.List;
import java.util.Optional;


@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    String SEARCH_PAGES_BY_FILTER_QUERY = """
            SELECT a FROM Agreement a
            WHERE (:transferor IS NULL OR :transferor = a.transferor) AND
            (:status IS NULL OR :status = a.status) AND
            a.transactionStatus = 'COMPLETED'
            """;

    @Query(value = SEARCH_PAGES_BY_FILTER_QUERY)
    Page<Agreement> findAllAgreements(@Param("transferor") String transferor,
                                      @Param("status") AgreementStatus status,
                                      Pageable pageable);

    @Override
    @Query("""
            SELECT a FROM Agreement a
            WHERE a.transactionStatus = 'COMPLETED'
            AND a.id = :id
            """)
    Optional<Agreement> findById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Agreement a SET a.transactionStatus = 'COMPLETED'
            WHERE a.id IN :ids
            """)
    void transactionCompletedAgreements(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query("""
            DELETE FROM Agreement a WHERE a.id in :ids
            """)
    void transactionNotCompletedAgreements(@Param("ids") List<Long> ids);
}
