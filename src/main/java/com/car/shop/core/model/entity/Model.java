package com.car.shop.core.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "model")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer productionStartYear;

    private Integer productionEndYear;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ShopPosition> shopPosition;

    public Model(Long id, String name, Integer productionStartYear, Integer productionEndYear) {
        this.id = id;
        this.name = name;
        this.productionStartYear = productionStartYear;
        this.productionEndYear = productionEndYear;
    }
}