package ru.pozhar.collector_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agreements_keys")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    @NotNull
    @Min(value = 1, message = "Ключ идемпотентности должен быть больше 0")
    @Max(value = 9223372036854775807L, message = "Ключ идемпотентности должен быть не более 9223372036854775807L")
    @Column(name = "key", nullable = false, unique = true)
    private Long key;
}
