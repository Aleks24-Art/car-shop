package com.car.shop.core.controller.v1;

import com.car.shop.core.model.dto.model.CreateModelDto;
import com.car.shop.core.model.dto.model.ModelDto;
import com.car.shop.core.model.dto.shop.position.CreateShopPositionDto;
import com.car.shop.core.model.dto.shop.position.ShopPositionDto;
import com.car.shop.core.model.entity.Mark;
import com.car.shop.core.model.entity.Model;
import com.car.shop.core.model.entity.ShopPosition;
import com.car.shop.core.service.mark.MarkService;
import com.car.shop.core.service.model.ModelService;
import com.car.shop.core.service.shop.position.ShopPositionService;
import com.car.shop.core.util.mapper.ShopPositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("")
public class ShopPositionController {

    @Autowired
    private ShopPositionService shopPositionService;

    @Autowired
    private MarkService markService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ShopPositionMapper shopPositionMapper;

    @GetMapping("v1/shop-position/all")
    public List<ShopPositionDto> findAllModels() {
        return shopPositionMapper.toDtos(shopPositionService.findAll());
    }

    @GetMapping("v1/shop-position/{id}")
    public ShopPositionDto findModelById(@PathVariable @Positive(message = "ID не может быть меньше нуля") Long id) {
        return shopPositionMapper.toDto(shopPositionService.findById(id));
    }

    @PostMapping("v1/shop-position")
    public ShopPositionDto saveShopPosition(@RequestBody @Valid CreateShopPositionDto createShopPositionDto) {
        Model model = modelService.findById(createShopPositionDto.getModelId());
        Mark mark = markService.findById(createShopPositionDto.getMarkId());
        ShopPosition shopPositionToSave = shopPositionMapper.toModel(createShopPositionDto, model, mark);
        ShopPosition savedShopPosition = shopPositionService.save(shopPositionToSave);
        return shopPositionMapper.toDto(savedShopPosition);
    }

    @DeleteMapping("v1/shop-position/{id}")
    public ResponseEntity<String> deleteModel(@PathVariable @Positive(message = "ID не может быть меньше нуля") Long id) {
        shopPositionService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
