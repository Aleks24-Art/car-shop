package com.car.shop.core.repository;

import com.car.shop.core.model.entity.ShopPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface ShopPositionRepository extends JpaRepository<ShopPosition, Long> {
}
