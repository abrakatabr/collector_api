package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.AgreementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ResponseAgreementDTO(Long id,
                                   BigDecimal originalDebtSum,
                                   BigDecimal actualDebtSum,
                                   LocalDate agreementStartDate,
                                   String transferor,
                                   AgreementStatus status,
                                   List<ResponseDebtorDTO> debtorsDTOs) {
}
