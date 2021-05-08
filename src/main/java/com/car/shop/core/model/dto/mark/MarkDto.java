package com.car.shop.core.model.dto.mark;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Setter
@Getter
@NoArgsConstructor
@ApiModel(description = "Марка авомобиля")
public class MarkDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID марки автомобиля", example = "12")
    private Long id;

    @ApiModelProperty(value = "Название марки", example = "Mercedes-Benz")
    private String name;

    @ApiModelProperty(value = "Страна-производитель", example = "Germany")
    private String producingCountry;
}