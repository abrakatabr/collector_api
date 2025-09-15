package ru.pozhar.collector_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "debtors_agreements")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtorAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Заемщик обязателеню")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debtor_id", nullable = false)
    private Debtor debtor;

    @NotNull(message = "Договор обязателен.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agreement_id", nullable = false)
    private Agreement agreement;

    @NotBlank(message = "Роль заемщика в договоре обязательна.")
    @Pattern(regexp = "co-debtor|single debtor|guarantor|charger",
            message = "Роль заемщика в договоре должна быть 'co-debtor', 'single debtor', 'guarantor' или 'charger'.")
    @Column(name = "role", nullable = false, length = 20)
    private String role;
}
