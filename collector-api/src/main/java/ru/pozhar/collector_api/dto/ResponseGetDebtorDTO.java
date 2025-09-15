package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.Gender;

import java.time.LocalDate;
import java.util.List;

public record ResponseGetDebtorDTO(Long id,
                                   String firstname,
                                   String lastname,
                                   String patronymic,
                                   LocalDate birthday,
                                   Gender gender,
                                   String phoneNumber,
                                   List<ResponseAddressDTO> addressDTOs,
                                   List<ResponseDocumentDTO> documentDTOs) {
}
