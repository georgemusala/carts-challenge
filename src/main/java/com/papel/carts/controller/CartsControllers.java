package com.papel.carts.controller;

import com.papel.carts.dto.CartData;
import com.papel.carts.service.CartsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that creates or updates a cart
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartsControllers {

    private final CartsService cartsService;

    /**
     * Creates a cart in database
     * @param cartData
     * @return
     */
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CartData createCart(@Valid @RequestBody CartData cartData) {
        return cartsService.createCart(cartData);
    }

    /**
     * Updates an existing cart from database
     * @param cartData
     * @return
     */
    @PostMapping(value = "/update", consumes = "application/json", produces = "application/json")
    public CartData updateCart(@Valid @RequestBody CartData cartData) {
        return cartsService.updateCart(cartData);
    }
}
