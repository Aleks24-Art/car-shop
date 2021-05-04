package com.car.shop.core.exception;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(Long id) {
        super("Model not found by id = " + id);
    }
}
