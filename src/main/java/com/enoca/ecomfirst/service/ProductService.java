package com.enoca.ecomfirst.service;
import com.enoca.ecomfirst.entity.Product;
import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(int theId);

    void save(Product theProduct);

    void deleteById(int theId);
}
