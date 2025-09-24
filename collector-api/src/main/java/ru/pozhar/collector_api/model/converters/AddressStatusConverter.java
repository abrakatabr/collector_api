package ru.pozhar.collector_api.model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.pozhar.collector_api.openapi.dto.AddressStatus;

@Converter(autoApply = true)
public class AddressStatusConverter implements AttributeConverter<AddressStatus, String> {
    @Override
    public String convertToDatabaseColumn(AddressStatus addressStatus) {
        if (addressStatus == null) {
            return null;
        }
        return addressStatus.getValue();
    }

    @Override
    public AddressStatus convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return AddressStatus.fromValue(s);
    }
}
