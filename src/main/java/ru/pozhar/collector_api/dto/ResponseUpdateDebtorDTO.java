package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.Gender;

import java.time.LocalDate;

public record ResponseUpdateDebtorDTO(
        Long id,
        String firstname,
        String lastname,
        String patronymic,
        LocalDate birthday,
        Gender gender,
        String phoneNumber) { }
