package ru.pozhar.collector_api.dto;

public record ResponseDocumentsDTO(String passportNumber,
                                   String inn,
                                   String snils) { }
