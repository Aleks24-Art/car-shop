package com.car.shop.core.service.shop.position;

import com.car.shop.core.model.entity.ShopPosition;

import java.util.List;

public interface ShopPositionService {
    List<ShopPosition> findAll();

    ShopPosition findById(Long id);

    ShopPosition save(ShopPosition shopPosition);

    void deleteById(Long id);
}
