package com.car.shop.core.service.model;

import com.car.shop.core.model.entity.Model;

import java.util.List;

public interface ModelService {
    List<Model> findAll();

    Model findById(Long id);

    Model save(Model model);

    Model update(Long id, Model model);

    void deleteById(Long id);
}
