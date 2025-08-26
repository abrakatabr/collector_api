package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.Gender;

import java.time.LocalDate;

public record RequestDebtorDTO(String firstname,
                               String lastname,
                               String patronymic,
                               String passportNumber,
                               RequestAddressDTO address,
                               LocalDate birthday,
                               Gender gender,
                               String phoneNumber) { }
