package ru.pozhar.collector_api.dto;

public record RequestAddressDTO(String country,
                                String city,
                                String street,
                                String house,
                                String apartment) { }
