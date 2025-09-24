package ru.pozhar.collector_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pozhar.collector_api.model.converters.DocumentTypeConverter;
import ru.pozhar.collector_api.openapi.dto.DocumentType;

import java.time.LocalDate;

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

    @Convert(converter = DocumentTypeConverter.class)
    @Column(name = "type", nullable = false)
    private DocumentType documentType;

    @NotBlank(message = "Номер документа обязателен")
    @Column(name = "document_number", nullable = false, length = 50)
    private String documentNumber;

    @NotNull(message = "Дата выдачи документа обязательна")
    @PastOrPresent(message = "Дата выдачи документа должна быть текущей датой или ранее")
    private LocalDate issueDate;
}
