package ru.pozhar.collector_api.dto;

public record ResponseAddressDTO(Long id,
                                 String country,
                                 String city,
                                 String street,
                                 String house,
                                 String apartment) {
}
