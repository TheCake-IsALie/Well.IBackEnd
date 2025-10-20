package org.generation.wellibackend.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter(autoApply = true)
public class ObjectNodeConverter implements AttributeConverter<ObjectNode, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ObjectNode attribute) {
        try {
            return attribute == null ? null : mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public ObjectNode convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : (ObjectNode) mapper.readTree(dbData);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

