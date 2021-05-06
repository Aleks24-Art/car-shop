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
@EqualsAndHashCode
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer productionStartYear;

    private Integer productionEndYear;

    @OneToMany
    @ToString.Exclude
    private List<ShopPosition> shopPosition;
}
