package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.DocumentType;

import java.time.LocalDate;

public record ResponseDocumentDTO(
        DocumentType documentType,
        String documentNumber,
        LocalDate issueDate) { }
