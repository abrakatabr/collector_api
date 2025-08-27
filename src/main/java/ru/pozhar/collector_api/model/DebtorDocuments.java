package ru.pozhar.collector_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "debtors_documents")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DebtorDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Заемщик обязателен.")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debtor_id", nullable = false, unique = true)
    private Debtor debtor;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "documents_id", nullable = false, unique = true)
    private Documents documents;
}
