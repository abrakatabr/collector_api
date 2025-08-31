package ru.pozhar.collector_api.dto;

public record ResponseUpdatePhoneDTO(
        Long debtorId,
        String phoneNumber) { }
