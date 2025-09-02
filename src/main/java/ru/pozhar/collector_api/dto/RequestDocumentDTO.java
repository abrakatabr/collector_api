package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.*;

import java.time.LocalDate;

public record RequestDocumentDTO(
        DocumentType documentType,
        String documentNumber,
        LocalDate issueDate) { }
