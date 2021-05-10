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

    @OneToMany(mappedBy = "mark", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ShopPosition> shopPosition;

    public Mark(Long id, String name, String producingCountry) {
        this.id = id;
        this.name = name;
        this.producingCountry = producingCountry;
    }
}
