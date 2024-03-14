package com.papel.carts.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.util.Currency;
import java.util.Date;

@Entity
@Table(name="carts")
@Data
public class CartModel implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, name = "date")
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column
    private Currency currency;

    @Column
    private double totalPrice;

    @Transient
    private boolean isNew = true;

    @Version
    private int version;
}
