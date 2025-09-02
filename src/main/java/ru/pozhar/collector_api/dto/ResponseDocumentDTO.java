package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.*;

import java.time.*;

public record ResponseDocumentDTO(
        DocumentType documentType,
        String documentNumber,
        LocalDate issueDate) { }
