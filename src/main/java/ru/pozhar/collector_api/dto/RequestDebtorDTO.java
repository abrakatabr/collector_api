package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.Gender;

import java.time.LocalDate;
import java.util.List;

public record RequestDebtorDTO(String firstname,
                               String lastname,
                               String patronymic,
                               LocalDate birthday,
                               Gender gender,
                               String role,
                               String phoneNumber,
                               List<RequestAddressDTO> addressDTOs,
                               List<RequestDocumentDTO> documentDTOs) { }
