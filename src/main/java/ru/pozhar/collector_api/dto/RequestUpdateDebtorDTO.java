package ru.pozhar.collector_api.dto;

import java.util.List;

public record RequestUpdateDebtorDTO(
        Long id,
        String phoneNumber,
        List<RequestAddressDTO> addressDTOs
) { }
