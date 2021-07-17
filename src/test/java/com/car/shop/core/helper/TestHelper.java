package com.car.shop.core.helper;

import com.car.shop.core.controller.v1.MarkController;
import com.car.shop.core.model.dto.mark.CreateMarkDto;
import com.car.shop.core.model.dto.mark.MarkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestHelper {

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private MarkController markController;

    public MarkDto saveMark(String fileName) throws Exception {
        CreateMarkDto createMarkDto = fileHelper.readFile(fileName, CreateMarkDto.class);
        return markController.saveMark(createMarkDto);
    }

    public String readFile(String filename) throws IOException {
        return fileHelper.readFile(filename);
    }

    public String asJsonString(Object object) throws IOException {
        return fileHelper.asJsonString(object);
    }
}
