package ru.pozhar.collector_api.repository;

import ru.pozhar.collector_api.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByCountryAndCityAndStreetAndHouseAndApartment(
            String country, String city, String street, String house, String apartment);

    default boolean existsByFullAddress(Address address) {
        return findByCountryAndCityAndStreetAndHouseAndApartment(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getApartment()
        ).isPresent();
    }
}
