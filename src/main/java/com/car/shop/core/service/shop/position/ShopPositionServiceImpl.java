package com.car.shop.core.service.shop.position;

import com.car.shop.core.exception.ShopPositionNotFoundException;
import com.car.shop.core.model.entity.ShopPosition;
import com.car.shop.core.repository.ShopPositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShopPositionServiceImpl implements ShopPositionService {

    @Autowired
    private ShopPositionRepository shopPositionRepository;

    @Override
    public List<ShopPosition> findAll() {
        log.info("Finding all shop positions");
        return shopPositionRepository.findAll();
    }

    @Override
    public ShopPosition findById(Long id) {
        log.info("Finding shop position by id: {}", id);
        return shopPositionRepository.findById(id)
                .orElseThrow(() -> new ShopPositionNotFoundException(id));
    }

    @Override
    public ShopPosition save(ShopPosition shopPosition) {
        log.info("Saving shop position: {}", shopPosition);
        return shopPositionRepository.save(shopPosition);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting model by id: {}", id);
        shopPositionRepository.deleteById(id);
    }
}
