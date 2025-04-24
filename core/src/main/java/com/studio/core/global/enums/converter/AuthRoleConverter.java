package com.studio.core.global.enums.converter;


import com.studio.core.global.enums.AuthRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AuthRoleConverter implements AttributeConverter<AuthRole, String> {

    @Override
    public String convertToDatabaseColumn(AuthRole attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public AuthRole convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return AuthRole.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown value for AuthRole: " + dbData);
        }
    }
}
