package com.car.shop.core.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileHelper {

    @Autowired
    private ObjectMapper objectMapper;

    public String readFile(String filename) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(filename);
        return objectMapper.readTree(stream).toString();
    }

    public <T> T readFile(String filename, Class<T> clazz) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(filename);
        return objectMapper.readValue(stream, clazz);
    }

    public <T> T readFile(String filename, TypeReference<T> valueTypeRef) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(filename);
        return objectMapper.readValue(stream, valueTypeRef);
    }

    public String asJsonString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }
}
