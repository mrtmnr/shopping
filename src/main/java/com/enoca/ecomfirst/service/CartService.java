package com.enoca.ecomfirst.service;

import com.enoca.ecomfirst.entity.Cart;

public interface CartService {
    Cart findById(int id);

    void save(Cart cart);

}
