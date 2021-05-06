package com.car.shop.core.repository;

import com.car.shop.core.model.entity.ShopPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopPositionRepository extends JpaRepository<ShopPosition, Long> {
}
