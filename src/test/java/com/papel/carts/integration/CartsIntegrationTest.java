package com.papel.carts.integration;

import com.papel.carts.model.CartModel;
import com.papel.carts.repository.CartsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CartsIntegrationTest{

    private static final String JSON_REQUEST = "{\n" +
            "    \"code\": \"2428380\",\n" +
            "    \"creationDate\": \"2023-09-13\",\n" +
            "    \"currency\": \"EUR\",\n" +
            "    \"totalPrice\": 102\n" +
            "}";

    private static final String JSON_RESPONSE = "{\"code\":\"2428380\",\"creationDate\":\"2023-09-13T00:00:00.000+00:00\",\"currency\":\"EUR\",\"totalPrice\":102.0}";

    private static final String CART_CODE = "2428380";
    private static final String CURRENCY = "EUR";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE = "2023-09-13";
    private static final double INITAL_PRICE = 102.0;
    private static final String CREATE_URI = "/carts/create";
    private static final String CONFLICT_RESPONSE_BODY = "Found cart with code: ";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartsRepository cartsRepository;

    private CartModel cartModel;

    @BeforeEach
    public void setUp() throws ParseException {
        cartModel = new CartModel();
        cartModel.setId(1L);
        cartModel.setId(1L);
        cartModel.setCode(CART_CODE);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = dateFormat.parse(DATE);
        cartModel.setCreationDate(date);
        cartModel.setCurrency(Currency.getInstance(CURRENCY));
        cartModel.setTotalPrice(INITAL_PRICE);
    }

    @Test
    public void shouldCreateCart() throws Exception {
        when(cartsRepository.findOneByCode(CART_CODE)).thenReturn(null);
        when(cartsRepository.save(any(CartModel.class))).thenReturn(cartModel);

        mockMvc.perform(post(CREATE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_REQUEST))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JSON_RESPONSE));
    }

    @Test
    public void shouldReturnCartNotFound() throws Exception {
        when(cartsRepository.findOneByCode(CART_CODE)).thenReturn(mock(CartModel.class));

        mockMvc.perform(post(CREATE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_REQUEST))
                .andExpect(status().isConflict())
                .andExpect(content().string(CONFLICT_RESPONSE_BODY + CART_CODE));
    }
}
