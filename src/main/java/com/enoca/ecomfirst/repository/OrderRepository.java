package com.enoca.ecomfirst.repository;

import com.enoca.ecomfirst.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {

}
