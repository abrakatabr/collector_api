package ru.pozhar.collector_api.model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.pozhar.collector_api.openapi.dto.Gender;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return Gender.UNKNOWN.getValue();
        }
        return gender.getValue();
    }

    @Override
    public Gender convertToEntityAttribute(String s) {
        if (s == null) {
            return Gender.UNKNOWN;
        }
        return Gender.fromValue(s);
    }
}
