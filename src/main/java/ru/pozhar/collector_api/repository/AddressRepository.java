package ru.pozhar.collector_api.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pozhar.collector_api.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    String SEARCH_BY_FILTER_QUERY = """
            SELECT * FROM addresses 
            WHERE (:debtorId IS NULL OR :debtorId = debtor_id) AND 
            (:country IS NULL OR :country = country) AND 
            (:city IS NULL OR :city = city) AND 
            (:street IS NULL OR :street = street) AND 
            (:house IS NULL OR :house = house) AND 
            (:apartment IS NULL OR :apartment = apartment) AND 
            (:addressStatus IS NULL OR :addressStatus = address_status)
            """;

    @Query(value = SEARCH_BY_FILTER_QUERY, nativeQuery = true)
    List<Address> findByFilters(@Param("debtorId") Long debtorId,
                                @Param("country") String country,
                                @Param("city") String city,
                                @Param("street") String street,
                                @Param("house") String house,
                                @Param("apartment") String apartment,
                                @Param("addressStatus") String addressStatus
                                );
    List<Address> findByDebtorId(Long debtorId);
}
