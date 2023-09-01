package com.enoca.ecomfirst.service;

import com.enoca.ecomfirst.entity.Promotion;

import java.util.List;

public interface PromotionService {
    List<Promotion> findAll();

    Promotion findById(int theId);

}
