package com.car.shop.core.controller.v1;

import com.car.shop.core.config.SwaggerConfig;
import com.car.shop.core.model.dto.model.CreateModelDto;
import com.car.shop.core.model.dto.model.ModelDto;
import com.car.shop.core.model.entity.Model;
import com.car.shop.core.service.model.ModelService;
import com.car.shop.core.util.mapper.ModelMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@Api(tags = SwaggerConfig.MODEL)
public class ModelController {

    @Autowired
    private ModelService modelService;

    @Autowired
    @Qualifier("carModelMapperImpl")
    private ModelMapper modelMapper;

    @ApiOperation("Получение всех моделей автомобилей")
    @GetMapping("/v1/model/all")
    public List<ModelDto> findAllModels() {
        return modelMapper.toDtos(modelService.findAll());
    }

    @ApiOperation(value = "Получение модели автомобиля по ID")
    @GetMapping("/v1/model/{id}")
    public ModelDto findModelById(
            @ApiParam("ID модели автомобиля")
            @PathVariable @NotNull @Positive(message = "ID должно быть больше 0") Long id) {
        return modelMapper.toDto(modelService.findById(id));
    }

    @ApiOperation(value = "Сохранение модели автомобиля")
    @PostMapping("/v1/model")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelDto saveModel(
            @ApiParam("Модель для сохранение модели авомобиля")
            @RequestBody @Valid CreateModelDto createModelDto) {
        Model modelToSave = modelMapper.toModel(createModelDto);
        Model savedModel = modelService.save(modelToSave);
        return modelMapper.toDto(savedModel);
    }

    @ApiOperation("Обновление модели автомобиля")
    @PutMapping("/v1/model/{id}")
    public ModelDto updateModel(
            @ApiParam("ID модели автомобиля")
            @PathVariable @NotNull @Positive(message = "ID должно быть больше 0") Long id,
            @ApiParam("Модель для обновления модели автомобиля")
            @RequestBody @Valid CreateModelDto updateModelDto) {
        Model modelToUpdate = modelMapper.toModel(updateModelDto);
        Model updatedModel = modelService.update(id, modelToUpdate);
        return modelMapper.toDto(updatedModel);
    }

    @ApiOperation("Удаление модели автомобиля")
    @DeleteMapping("/v1/model/{id}")
    public ResponseEntity<String> deleteModel(
            @ApiParam("ID модели автомобиля")
            @PathVariable @NotNull @Positive(message = "ID должно быть больше 0") Long id) {
        modelService.findById(id);
        modelService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
