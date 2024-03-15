package com.papel.carts.service.impl;

import com.papel.carts.dto.CartData;
import com.papel.carts.exception.CartExistsException;
import com.papel.carts.exception.CartNotFoundException;
import com.papel.carts.model.CartModel;
import com.papel.carts.repository.CartsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.util.SerializationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DefaultCartServiceTest {

    private static final String CART_CODE = "2428380";
    private static final String CURRENCY = "EUR";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String REQUEST_DATE = "2023-09-13";
    private static final String EXPECTED_DATE = "2024-09-13T00:00:00.000+00:00";
    private static final double INITAL_PRICE = 102.45;
    private static final double FINAL_PRICE = 150;

    @InjectMocks
    private DefaultCartsService defaultCartService;

    @Mock
    private CartsRepository cartsRepository;

    @Mock
    private ModelMapper modelMapper;

    private CartData cartDataRequest;
    private CartData expectedCartData;
    private CartModel cartModel;

    @BeforeEach
    public void setUp() throws ParseException {
        cartDataRequest = new CartData();
        cartDataRequest.setCode(CART_CODE);
        cartDataRequest.setCurrency(Currency.getInstance(CURRENCY));
        cartDataRequest.setTotalPrice(INITAL_PRICE);

        expectedCartData = SerializationUtils.clone(cartDataRequest);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = dateFormat.parse(REQUEST_DATE);
        Date expectedDate = dateFormat.parse(EXPECTED_DATE);

        cartDataRequest.setCreationDate(date);
        expectedCartData.setCreationDate(expectedDate);

        cartModel = new CartModel();
        CartModel cartModel = new CartModel();
        cartModel.setId(1L);
        cartModel.setCode(CART_CODE);
        cartModel.setCreationDate(cartDataRequest.getCreationDate());
        cartModel.setCurrency(Currency.getInstance(CURRENCY));
        cartModel.setTotalPrice(INITAL_PRICE);
    }

    @Test
    public void shouldCreateCart() {

        when(cartsRepository.findOneByCode(CART_CODE)).thenReturn(null);
        when(modelMapper.map(cartDataRequest, CartModel.class)).thenReturn(cartModel);
        when(cartsRepository.save(cartModel)).thenReturn(cartModel);
        when(modelMapper.map(cartModel, CartData.class)).thenReturn(expectedCartData);

        CartData createdCart = defaultCartService.createCart(cartDataRequest);

        verify(cartsRepository).findOneByCode(anyString());
        verify(cartsRepository).save(any(CartModel.class));

        assertTrue(new ReflectionEquals(expectedCartData).matches(createdCart));
    }

    @Test
    public void shouldThrowCartExistsExceptionOnCreateCart() {

        when(cartsRepository.findOneByCode(CART_CODE)).thenReturn(mock(CartModel.class));

        assertThrows(CartExistsException.class, () ->defaultCartService.createCart(cartDataRequest));

        verify(cartsRepository).findOneByCode(anyString());
    }

    @Test
    public void shouldUpdateCart() {

        when(cartsRepository.findOneByCode(CART_CODE)).thenReturn(cartModel);

        doAnswer(invocation -> {
            cartModel.setTotalPrice(FINAL_PRICE);
            return null;
        }).when(modelMapper).map(cartDataRequest, cartModel);

        when(cartsRepository.save(cartModel)).thenReturn(cartModel);
        expectedCartData.setTotalPrice(FINAL_PRICE);
        when(modelMapper.map(cartModel, CartData.class)).thenReturn(expectedCartData);

        CartData updatedCart = defaultCartService.updateCart(cartDataRequest.getCode(), cartDataRequest);

        verify(cartsRepository).save(any(CartModel.class));

        assertEquals(FINAL_PRICE, updatedCart.getTotalPrice());
    }


    @Test
    public void shouldThrowCartNotFoundExceptionOnUpdateCart() {

        when(cartsRepository.findOneByCode(CART_CODE)).thenReturn(null);

        assertThrows(CartNotFoundException.class, () ->defaultCartService.updateCart(cartDataRequest.getCode(), cartDataRequest));
    }

}
