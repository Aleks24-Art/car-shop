package com.car.shop.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShopPositionNotFoundException extends RuntimeException {

    public ShopPositionNotFoundException(Long id) {
        super("Shop position not found by id = " + id);
    }
}
