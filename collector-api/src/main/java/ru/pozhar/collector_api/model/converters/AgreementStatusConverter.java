package ru.pozhar.collector_api.model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.pozhar.collector_api.openapi.dto.AgreementStatus;

@Converter(autoApply = true)
public class AgreementStatusConverter implements AttributeConverter<AgreementStatus, String> {
    @Override
    public String convertToDatabaseColumn(AgreementStatus agreementStatus) {
        if (agreementStatus == null) {
            return null;
        }
        return agreementStatus.getValue();
    }

    @Override
    public AgreementStatus convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return AgreementStatus.fromValue(s);
    }
}
