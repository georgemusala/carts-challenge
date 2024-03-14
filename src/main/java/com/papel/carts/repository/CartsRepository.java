package com.papel.carts.repository;

import com.papel.carts.model.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartsRepository extends JpaRepository<CartModel, Long> {

    CartModel findOneByCode(String code);

    CartModel save(CartModel cartModel);
}
