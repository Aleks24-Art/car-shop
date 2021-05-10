package com.car.shop.core.service.shop.position;

import com.car.shop.core.exception.ShopPositionNotFoundException;
import com.car.shop.core.model.entity.ShopPosition;
import com.car.shop.core.repository.ShopPositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShopPositionServiceImpl implements ShopPositionService {

    @Autowired
    private ShopPositionRepository shopPositionRepository;

    @Override
    public List<ShopPosition> searchByText(String searchText, Sort sort) {
        if (searchText == null || searchText.isEmpty()) {
            return shopPositionRepository.findAll();
        }
        searchText = searchText.trim();
        Specification<ShopPosition> shopPositionSpecification =
                shopPositionMarkNameHas(searchText)
                        .or(shopPositionModelNameHas(searchText))
                        .or(shopPositionMarkProducingCountryHas(searchText));

        return shopPositionRepository.findAll(shopPositionSpecification, sort);
    }

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
    public ShopPosition update(Long id, ShopPosition newShopPosition) {
        log.info("Updating shop position by id: {} and body {} ", id, newShopPosition);
        return shopPositionRepository.findById(id)
                .map(shopPosition -> {
                            shopPosition.setId(id);
                            shopPosition.setMark(newShopPosition.getMark());
                            shopPosition.setModel(newShopPosition.getModel());
                            shopPosition.setPrice(newShopPosition.getPrice());
                            shopPosition.setKilometrage(newShopPosition.getKilometrage());
                            shopPosition.setProducedYear(newShopPosition.getProducedYear());
                            return shopPositionRepository.save(shopPosition);
                        }
                ).orElseThrow(
                        () -> new ShopPositionNotFoundException(id)
                );
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting model by id: {}", id);
        shopPositionRepository.deleteById(id);
    }

    private static Specification<ShopPosition> shopPositionMarkNameHas(String markName) {
        return (position, cq, cb) -> cb.like(position.get("mark").get("name"), "%" + markName + "%");
    }

    private static Specification<ShopPosition> shopPositionMarkProducingCountryHas(String prodCountry) {
        return (position, cq, cb) -> cb.like(position.get("mark").get("producingCountry"), "%" + prodCountry + "%");
    }

    private static Specification<ShopPosition> shopPositionModelNameHas(String modelName) {
        return (position, cq, cb) -> cb.like(position.get("model").get("name"), "%" + modelName + "%");
    }
}
