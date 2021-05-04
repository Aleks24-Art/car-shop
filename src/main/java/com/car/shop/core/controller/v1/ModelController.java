package com.car.shop.core.controller.v1;

import com.car.shop.core.model.dto.model.CreateModelDto;
import com.car.shop.core.model.dto.model.ModelDto;
import com.car.shop.core.model.entity.Model;
import com.car.shop.core.service.model.ModelService;
import com.car.shop.core.util.mapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @Autowired
    @Qualifier("carModelMapperImpl")
    private ModelMapper modelMapper;

    @GetMapping("v1/model/all")
    public List<ModelDto> findAllModels() {
        return modelMapper.toDtos(modelService.findAll());
    }

    @GetMapping("v1/model/{id}")
    public ModelDto findModelById(@PathVariable @Positive(message = "ID не может быть меньше нуля") Long id) {
        return modelMapper.toDto(modelService.findById(id));
    }

    @PostMapping("v1/model")
    public ModelDto saveModel(@RequestBody @Valid CreateModelDto createModelDto) {
        Model modelToSave = modelMapper.toModel(createModelDto);
        Model savedModel = modelService.save(modelToSave);
        return modelMapper.toDto(savedModel);
    }

    @DeleteMapping("v1/model/{id}")
    public ResponseEntity<String> deleteModel(@PathVariable @Positive(message = "ID не может быть меньше нуля") Long id) {
        modelService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
