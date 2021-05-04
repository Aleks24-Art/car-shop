package com.car.shop.core.model.dto.mark;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ToString
@Setter
@Getter
@NoArgsConstructor
@ApiModel(description = "Создания марки авомобиля")
public class CreateMarkDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Название марки не может быть пустым")
    @ApiModelProperty(value = "Название марки", example = "Mercedes-Benz")
    private String name;

    @NotBlank(message = "Название страны-производителя не может быть пустым")
    @ApiModelProperty(value = "Страна-производитель", example = "Germany")
    private String producingCountry;
}