package com.car.shop.core.controller.v1;

import com.car.shop.core.config.SwaggerConfig;
import com.car.shop.core.model.dto.shop.position.CreateShopPositionDto;
import com.car.shop.core.model.dto.shop.position.ShopPositionDto;
import com.car.shop.core.model.entity.Mark;
import com.car.shop.core.model.entity.Model;
import com.car.shop.core.model.entity.ShopPosition;
import com.car.shop.core.service.mark.MarkService;
import com.car.shop.core.service.model.ModelService;
import com.car.shop.core.service.shop.position.ShopPositionService;
import com.car.shop.core.util.mapper.ShopPositionMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("")
@Validated
@Api(tags = SwaggerConfig.SHOP_POSITION)
public class ShopPositionController {

    @Autowired
    private ShopPositionService shopPositionService;

    @Autowired
    private MarkService markService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ShopPositionMapper shopPositionMapper;

    @ApiOperation("Поиск позиций в магазине с возможностью сортировки." +
            "Условия: имя марки, имя модели, страна-производитель" +
            "При отсутствии searchText вернутся все позиции в магазине")
    @GetMapping("v1/shop-position/search")
    public List<ShopPositionDto> getShopPositionList(
            @RequestParam(required = false, defaultValue = "ASC", name = "Порядок сортировки") Sort.Direction order,
            @RequestParam(required = false, defaultValue = "producedYear", name = "Имя колонки для сортировки") String columnToSort,
            @RequestParam(required = false, name = "Текст для поиска") String searchText
    ) {
        Sort sort = Sort.by(order, columnToSort);
        List<ShopPosition> shopPositions = shopPositionService.searchByText(searchText, sort);
        return shopPositionMapper.toDtos(shopPositions);
    }

    @ApiOperation("Получение всех позиций в магазине")
    @GetMapping("v1/shop-position/all")
    public List<ShopPositionDto> findAllShopPositions() {
        return shopPositionMapper.toDtos(shopPositionService.findAll());
    }

    @ApiOperation("Получение позиции в магазине по ID")
    @GetMapping("v1/shop-position/{id}")
    public ShopPositionDto findShopPositionById(
            @ApiParam("ID позиции в магазине")
            @PathVariable @Positive(message = "ID должно быть больше 0") Long id) {
        return shopPositionMapper.toDto(shopPositionService.findById(id));
    }

    @ApiOperation("Создание новой позиции в магазине")
    @PostMapping("v1/shop-position")
    public ShopPositionDto saveShopPosition(
            @ApiParam("Модель для создания позиции в магазине")
            @RequestBody @Valid CreateShopPositionDto createShopPositionDto) {
        Model model = modelService.findById(createShopPositionDto.getModelId());
        Mark mark = markService.findById(createShopPositionDto.getMarkId());
        ShopPosition shopPositionToSave = shopPositionMapper.toModel(createShopPositionDto, model, mark);
        ShopPosition savedShopPosition = shopPositionService.save(shopPositionToSave);
        return shopPositionMapper.toDto(savedShopPosition);
    }

    @ApiOperation("Удаление позиции из магазина")
    @DeleteMapping("v1/shop-position/{id}")
    public ResponseEntity<String> deleteShopPosition(
            @ApiParam("ID позиции в магазине")
            @PathVariable @Positive(message = "ID должно быть больше 0") Long id) {
        shopPositionService.findById(id);
        shopPositionService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
