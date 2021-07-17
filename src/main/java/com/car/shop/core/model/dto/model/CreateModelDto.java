package com.car.shop.core.model.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Создания модели авомобиля")
public class CreateModelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Название модели не может быть пустым")
    @ApiModelProperty(value = "Название модели", example = "S-Class w223")
    private String name;

    @PositiveOrZero(message = "Год начала производства модели не может быть отрицательным")
    @NotNull(message = "Год начала производства модели не может быть пустым")
    @ApiModelProperty(value = "Год начала производства", example = "1989")
    private Integer productionStartYear;

    @PositiveOrZero(message = "Год окончания производства модели не может быть отрицательным")
    @ApiModelProperty(value = "Год окончания производства", example = "2017")
    private Integer productionEndYear;
}