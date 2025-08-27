package ru.pozhar.collector_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name = "debtors_addresses")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DebtorAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Заемщик обязателен.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debtor_id", nullable = false)
    private Debtor debtor;

    @NotNull(message = "Адрес обязателен.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @NotBlank(message = "Статус обязателеню")
    @Pattern(regexp = "registration|residential",
            message = "Статус адрема должен быть 'registration' или 'residential'.")
    @Column(name = "address_status", nullable = false)
    private String addressStatus = "registration";
}
