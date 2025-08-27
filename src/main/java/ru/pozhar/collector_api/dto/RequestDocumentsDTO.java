package ru.pozhar.collector_api.dto;

public record RequestDocumentsDTO(String passportNumber,
                                  String inn,
                                  String snils) { }
