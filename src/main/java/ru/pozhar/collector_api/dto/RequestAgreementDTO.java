package ru.pozhar.collector_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RequestAgreementDTO(
        @NotNull(message = "Сумма долга по договору обязательна")
        @DecimalMin(value = "0.0", message = "Сумма долга по договору не может быть отрицательной")
        BigDecimal originalDebtSum,
        @NotNull(message = "Текущий остаток по договору обязателен")
        @DecimalMin(value = "0.0", message = "Текущий остаток по договору не может быть отрицательным")
        BigDecimal actualDebtSum,
        @NotNull(message = "Дата подписания договора обязательна")
        @PastOrPresent(message = "Дата подписания договора должна быть текущей датой или ранее")
        LocalDate agreementStartDate,
        @NotBlank(message = "Название банка-кредитора обязательно")
        String transferor,
        @Valid
        List<@Valid RequestDebtorDTO> debtors) { }
