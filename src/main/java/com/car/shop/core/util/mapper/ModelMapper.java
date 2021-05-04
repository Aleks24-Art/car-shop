package com.car.shop.core.util.mapper;

import com.car.shop.core.model.dto.model.CreateModelDto;
import com.car.shop.core.model.dto.model.ModelDto;
import com.car.shop.core.model.entity.Model;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD, implementationName = "CarModelMapperImpl")
public interface ModelMapper {

    List<ModelDto> toDtos(List<Model> models);

    ModelDto toDto(Model model);

    Model toModel(CreateModelDto createModelDto);
}
