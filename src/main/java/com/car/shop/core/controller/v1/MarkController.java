package com.car.shop.core.controller.v1;

import com.car.shop.core.config.SwaggerConfig;
import com.car.shop.core.model.dto.mark.CreateMarkDto;
import com.car.shop.core.model.dto.mark.MarkDto;
import com.car.shop.core.model.entity.Mark;
import com.car.shop.core.service.mark.MarkService;
import com.car.shop.core.util.mapper.MarkMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Validated
@Api(tags = SwaggerConfig.MARK)
public class MarkController {

    @Autowired
    private MarkService markService;

    @Autowired
    private MarkMapper markMapper;

    @ApiOperation("Получение всех марок автомобилей")
    @GetMapping("/v1/mark/all")
    public List<MarkDto> findAllMarks() {
        return markMapper.toDtos(markService.findAll());
    }

    @ApiOperation("Получение марки автомобиля по ID")
    @GetMapping("/v1/mark/{id}")
    public MarkDto findMarkById(
            @ApiParam("ID марки автомобиля")
            @PathVariable @NotNull @Positive(message = "ID должно быть больше 0") Long id) {
        return markMapper.toDto(markService.findById(id));
    }

    @ApiOperation("Сохранение марки автомобиля")
    @PostMapping("/v1/mark")
    @ResponseStatus(HttpStatus.CREATED)
    public MarkDto saveMark(
            @ApiParam("Модель для сохранение марки автомобиля")
            @RequestBody @Valid CreateMarkDto createMarkDto) {
        Mark markToSave = markMapper.toMark(createMarkDto);
        Mark savedMark = markService.save(markToSave);
        return markMapper.toDto(savedMark);
    }

    @ApiOperation("Обновление марки автомобиля")
    @PutMapping("/v1/mark/{id}")
    public MarkDto updateMark(
            @ApiParam("ID марки автомобиля")
            @PathVariable @NotNull @Positive(message = "ID должно быть больше 0") Long id,
            @ApiParam("Модель для обновления марки автомобиля")
            @RequestBody @Valid CreateMarkDto updateMarkDto) {
        Mark markToUpdate = markMapper.toMark(updateMarkDto);
        Mark updatedMark = markService.update(id, markToUpdate);
        return markMapper.toDto(updatedMark);
    }

    @ApiOperation("Удаление марки автомобиля")
    @DeleteMapping("/v1/mark/{id}")
    public ResponseEntity<String> deleteMark(
            @ApiParam("ID марки автомобиля")
            @PathVariable @NotNull @Positive(message = "ID должно быть больше 0") Long id) {
        markService.findById(id);
        markService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
