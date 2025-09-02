package ru.pozhar.collector_api.dto;

import ru.pozhar.collector_api.model.*;

import java.time.*;

public record RequestDocumentDTO(
        DocumentType documentType,
        String documentNumber,
        LocalDate issueDate) { }
