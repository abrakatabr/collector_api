package ru.pozhar.collector_api.dto;

public record ResponseUpdateAddressDTO(
        Long debtorId,
        String country,
        String city,
        String street,
        String house,
        String apartment,
        String addressStatus
) { }
