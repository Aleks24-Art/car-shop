package com.car.shop.core.model.dto.shop.position;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

@ToString
@Setter
@Getter
@NoArgsConstructor
@ApiModel(description = "Создания позиции в магазине")
public class CreateShopPositionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Positive
    @ApiModelProperty(value = "ID марки автомобиля", example = "1")
    private Long markId;

    @Positive
    @ApiModelProperty(value = "ID модели автомобиля", example = "1")
    private Long modelId;

    @PositiveOrZero
    @ApiModelProperty(value = "Год выпуска автомобиля", example = "2008")
    private Integer producedYear;

    @PositiveOrZero
    @ApiModelProperty(value = "Пробег автомобиля", example = "256")
    private Integer kilometrage;

    @PositiveOrZero
    @ApiModelProperty(value = "Цена автомобиля", example = "10599.54")
    private BigDecimal price;
}
