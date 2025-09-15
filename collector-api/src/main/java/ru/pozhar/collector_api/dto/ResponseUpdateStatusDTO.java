package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.AgreementStatus;

public record ResponseUpdateStatusDTO(
        Long agreementId,
        AgreementStatus status
) { }
