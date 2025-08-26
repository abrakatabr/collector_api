package ru.pozhar.collector_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ResponseAgreementDTO(Long id,
                                   BigDecimal originalDebtSum,
                                   BigDecimal actualDebtSum,
                                   LocalDate agreementStartDate,
                                   String transferor,
                                   List<ResponseDebtorDTO> debtors) {
}
