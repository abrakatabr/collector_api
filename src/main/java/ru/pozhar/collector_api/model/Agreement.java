package ru.pozhar.collector_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "agreements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Сумма долга по договору обязательна.")
    @DecimalMin(value = "0.0", message = "Сумма долга по договору не может быть отрицательной.")
    @Column(name = "original_debt_sum", nullable = false, precision = 15, scale = 2)
    private BigDecimal originalDebtSum;

    @NotNull(message = "Текущий остаток по договору обязателен.")
    @DecimalMin(value = "0.0", message = "Текущий остаток по договору не может быть отрицательным.")
    @Column(name = "actual_debt_sum", nullable = false, precision = 15, scale = 2)
    private BigDecimal actualDebtSum;

    @NotNull(message = "Дата подписания договора обязательна.")
    @PastOrPresent(message = "Дата подписания договора должна быть текущей датой или ранее.")
    private LocalDate agreementStartDate;

    @NotBlank(message = "Название банка-кредитора обязательно.")
    @Column(name = "transferor", nullable = false, length = 128)
    private String transferor;
}
