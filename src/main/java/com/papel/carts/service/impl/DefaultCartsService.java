package com.papel.carts.service.impl;

import com.papel.carts.dto.CartData;
import com.papel.carts.exception.CartExistsException;
import com.papel.carts.exception.CartNotFoundException;
import com.papel.carts.model.CartModel;
import com.papel.carts.repository.CartsRepository;
import com.papel.carts.service.CartsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCartsService implements CartsService {
    private static final String MISSING_CART_MESSAGE = "No cart found by code: %s";
    private static final String CART_EXISTS = "Found cart with code: %s";

    private final CartsRepository cartsRepository;

    private final ModelMapper modelMapper;

    @Override
    public CartData createCart(final CartData cartData) {
        if (cartsRepository.findOneByCode(cartData.getCode()) != null) {
            throw new CartExistsException(CART_EXISTS.formatted(cartData.getCode()));
        }
        CartModel savedCart = saveCart(modelMapper.map(cartData, CartModel.class));

        return modelMapper.map(savedCart, CartData.class);
    }

    @Override
    public CartData updateCart(final String cartCode, final CartData cartData) {

        cartData.setCode(cartCode);
        CartModel existingCart = cartsRepository.findOneByCode(cartCode);
        if (existingCart == null) {
            throw new CartNotFoundException(MISSING_CART_MESSAGE.formatted(cartCode));
        }

        modelMapper.map(cartData, existingCart);
        existingCart.setNew(false);

        return modelMapper.map(saveCart(existingCart), CartData.class);
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private CartModel saveCart(CartModel cartModel) throws ObjectOptimisticLockingFailureException {
        CartModel savedCart = cartsRepository.save(cartModel);
        return savedCart;
    }
}
