package com.car.shop.core.service.model;

import com.car.shop.core.exception.ModelNotFoundException;
import com.car.shop.core.model.entity.Model;
import com.car.shop.core.repository.ModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Override
    public List<Model> findAll() {
        log.info("Finding all models");
        return modelRepository.findAll();
    }

    @Override
    public Model findById(Long id) {
        log.info("Finding model by id: {}", id);
        return modelRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @Override
    public Model save(Model model) {
        log.info("Saving model: {}", model);
        return modelRepository.save(model);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting model by id: {}", id);
        modelRepository.deleteById(id);
    }
}
