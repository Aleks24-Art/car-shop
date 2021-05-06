package com.car.shop.core.util.mapper;

import com.car.shop.core.model.dto.shop.position.CreateShopPositionDto;
import com.car.shop.core.model.dto.shop.position.ShopPositionDto;
import com.car.shop.core.model.entity.Mark;
import com.car.shop.core.model.entity.Model;
import com.car.shop.core.model.entity.ShopPosition;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface ShopPositionMapper {

    List<ShopPositionDto> toDtos(List<ShopPosition> shopPositions);

    ShopPositionDto toDto(ShopPosition shopPosition);

    @Mapping(target = "id", ignore = true)
    ShopPosition toModel(CreateShopPositionDto createShopPositionDto, Model model, Mark mark);
}
