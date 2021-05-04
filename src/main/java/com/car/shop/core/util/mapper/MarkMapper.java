package com.car.shop.core.util.mapper;

import com.car.shop.core.model.dto.mark.CreateMarkDto;
import com.car.shop.core.model.dto.mark.MarkDto;
import com.car.shop.core.model.entity.Mark;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface MarkMapper {

    List<MarkDto> toDtos(List<Mark> maks);

    MarkDto toDto(Mark mark);

    Mark toMark(CreateMarkDto createMarkDto);
}
