package com.car.shop.core.repository;

import com.car.shop.core.model.entity.ShopPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopPositionRepository extends JpaRepository<ShopPosition, Long> {
    List<ShopPosition> findAll(Specification<ShopPosition> spec, Sort sort);
}
