package com.papel.carts.service;

import com.papel.carts.dto.CartData;

/**
 * Service for cart operations
 */
public interface CartsService {

    /**
     * Creates a cart in database
     * @param cartData
     * @return
     */
    CartData createCart(final CartData cartData);

    /**
     * Updates an existing cart from database
     * @param cartData
     * @return
     */
    CartData updateCart(final String cartCode, final CartData cartData);
}
