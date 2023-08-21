package com.enoca.ecomfirst.repository;
import com.enoca.ecomfirst.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}
