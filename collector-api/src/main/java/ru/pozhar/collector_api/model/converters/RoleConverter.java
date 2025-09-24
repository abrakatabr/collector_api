package ru.pozhar.collector_api.model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.pozhar.collector_api.openapi.dto.AddressStatus;
import ru.pozhar.collector_api.openapi.dto.Role;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null;
        }
        return role.getValue();
    }

    @Override
    public Role convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return Role.fromValue(s);
    }
}
