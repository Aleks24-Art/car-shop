package com.car.shop.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ModelNotFoundException extends RuntimeException {

    public ModelNotFoundException(Long id) {
        super("Model not found by id = " + id);
    }
}
