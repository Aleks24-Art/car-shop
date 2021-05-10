package com.car.shop.core.service.shop.position;

import com.car.shop.core.model.entity.ShopPosition;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ShopPositionService {
    List<ShopPosition> searchByText(String searchText, Sort sort);

    List<ShopPosition> findAll();

    ShopPosition findById(Long id);

    ShopPosition save(ShopPosition shopPosition);

    ShopPosition update(Long id, ShopPosition newShopPosition);

    void deleteById(Long id);
}
