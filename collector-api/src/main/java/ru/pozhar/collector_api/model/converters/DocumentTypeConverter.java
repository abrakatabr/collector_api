package ru.pozhar.collector_api.model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.pozhar.collector_api.openapi.dto.DocumentType;

@Converter
public class DocumentTypeConverter implements AttributeConverter<DocumentType, String> {
    @Override
    public String convertToDatabaseColumn(DocumentType documentType) {
        if (documentType == null) {
            return null;
        }
        return documentType.getValue();
    }

    @Override
    public DocumentType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return DocumentType.fromValue(s);
    }
}
