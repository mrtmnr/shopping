package com.enoca.ecomfirst.repository;

import com.enoca.ecomfirst.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    
}
