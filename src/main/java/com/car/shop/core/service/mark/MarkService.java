package com.car.shop.core.service.mark;

import com.car.shop.core.model.entity.Mark;

import java.util.List;

public interface MarkService {

    List<Mark> findAll();

    Mark findById(Long id);

    Mark save(Mark mark);

    void deleteById(Long id);
}
