package ru.pozhar.collector_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.*;

@Entity
@Table(name = "documents")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID заемщика обязателен")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debtor_id", nullable = false)
    private Debtor debtor;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DocumentType documentType;

    @NotBlank(message = "Номер документа обязателен")
    @Column(name = "document_number", nullable = false, length = 50)
    private String documentNumber;

    @NotNull(message = "Дата выдачи документа обязательна")
    @PastOrPresent(message = "Дата выдачи документа должна быть текущей датой или ранее")
    private LocalDate issueDate;
}
