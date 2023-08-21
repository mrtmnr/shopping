package com.enoca.ecomfirst.service;
import com.enoca.ecomfirst.repository.CartRepository;
import com.enoca.ecomfirst.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    private CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart findById(int theId) {
        Optional<Cart> result = cartRepository.findById(theId);

        Cart theCart = null;

        if (result.isPresent()) {
            theCart = result.get();
        }
        else {
            // we didn't find the Product
            throw new RuntimeException("Did not find employee id - " + theId);
        }

        return theCart;
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }
}
