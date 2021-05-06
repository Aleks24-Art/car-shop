package com.car.shop.core.model.dto.shop.position;

import com.car.shop.core.model.dto.mark.MarkDto;
import com.car.shop.core.model.dto.model.ModelDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@ToString
@Setter
@Getter
@NoArgsConstructor
@ApiModel(description = "Позиция в магазине")
public class ShopPositionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID позиции в магазине", example = "10")
    private Long id;

    @ApiModelProperty(value = "Информация о модели автомобиля")
    private MarkDto mark;

    @ApiModelProperty(value = "Информация о марке автомобиля")
    private ModelDto model;

    @ApiModelProperty(value = "Год выпуска автомобиля", example = "2008")
    private Integer producedYear;

    @ApiModelProperty(value = "Пробег автомобиля", example = "256")
    private Integer kilometrage;

    @ApiModelProperty(value = "Цена автомобиля", example = "10599.54")
    private BigDecimal price;
}