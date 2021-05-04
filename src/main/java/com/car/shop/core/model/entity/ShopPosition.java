package com.car.shop.core.model.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "shop-position")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ShopPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mark_id")
    private Mark mark;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    private Model model;

    private Integer producedYear;

    private Integer kilometrage;

    private BigDecimal price;

}
