package com.car.shop.core.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "mark")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String producingCountry;

    @OneToMany
    @ToString.Exclude
    private List<ShopPosition> shopPosition;
}
