package com.car.shop.core.handlers;

import com.car.shop.core.exception.MarkNotFoundException;
import com.car.shop.core.exception.ModelNotFoundException;
import com.car.shop.core.exception.ShopPositionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(v -> v.getRootBeanClass().getName() + " " + v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());
        ApiError result = ApiErrorFactory.badRequest(request, "A constraint violation has occurred", errors);
        log.error("Constraint violation. Response: {}", result);
        return result;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            MarkNotFoundException.class,
            ModelNotFoundException.class,
            ShopPositionNotFoundException.class})
    public ApiError handleNonExistentMark(RuntimeException ex, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ApiError result = ApiErrorFactory.badRequest(request, ex.getMessage(), errors);
        log.error("Entity not found exception. Response: {}", result);
        return result;
    }
}
