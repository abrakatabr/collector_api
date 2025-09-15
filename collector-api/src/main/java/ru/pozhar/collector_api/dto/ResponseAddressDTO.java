package ru.pozhar.collector_api.dto;

public record ResponseAddressDTO(
        String country,
        String city,
        String street,
        String house,
        String apartment,
        String addressStatus) { }
