package ru.pozhar.collector_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import ru.pozhar.collector_api.model.DocumentType;

import java.time.LocalDate;

public record RequestDocumentDTO(
        @NotNull
        DocumentType documentType,
        @NotBlank(message = "Номер документа обязателен")
        String documentNumber,
        @NotNull(message = "Дата выдачи документа обязательна")
        @PastOrPresent(message = "Дата выдачи документа должна быть текущей датой или ранее")
        LocalDate issueDate) { }
