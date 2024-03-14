package com.papel.carts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;

@Data
public class CartData implements Serializable {

    @NotBlank(message = "The code shouldn't be empty")
    private String code;

    @NotNull(message = "The creationDate shouldn't be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date creationDate;

    private Currency currency;

    private double totalPrice;
}
