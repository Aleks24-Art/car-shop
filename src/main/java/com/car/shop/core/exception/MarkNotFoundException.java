package com.car.shop.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MarkNotFoundException extends RuntimeException {

    public MarkNotFoundException(Long id) {
        super("Mark not found by id = " + id);
    }
}
