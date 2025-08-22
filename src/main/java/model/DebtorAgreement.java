package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "debtors_agreements")
@Getter
@Setter
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
    @Pattern(regexp = "co-debtor|single debtor",
            message = "Роль заемщика в договоре должна быть 'co-debtor' или 'single debtor'.")
    @Column(name = "debtor_type", nullable = false, length = 20)
    private String debtorType;
}
