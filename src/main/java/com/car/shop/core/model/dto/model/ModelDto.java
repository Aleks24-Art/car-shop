package com.car.shop.core.model.dto.model;

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
@ApiModel(description = "Модель авомобиля")
public class ModelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID", example = "24")
    private Long id;

    @ApiModelProperty(value = "Название модели", example = "S-Class w223")
    private String name;

    @ApiModelProperty(value = "Год начала производства", example = "1989")
    private Integer productionStartYear;

    @ApiModelProperty(value = "Год окончания производства", example = "2017")
    private Integer productionEndYear;
}