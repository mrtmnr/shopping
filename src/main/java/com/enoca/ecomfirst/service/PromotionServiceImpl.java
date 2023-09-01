package com.enoca.ecomfirst.service;

import com.enoca.ecomfirst.entity.Promotion;
import com.enoca.ecomfirst.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService{

    private PromotionRepository promotionRepository;
    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository=promotionRepository;
    }

    @Override
    public List<Promotion> findAll() {
         return promotionRepository.findAll();
    }

    @Override
    public Promotion findById(int theId) {
        Optional<Promotion> result = promotionRepository.findById(theId);

        Promotion thePromotion = null;

        if (result.isPresent()) {
            thePromotion = result.get();
        }
        else {
            // we didn't find the Promotion
            throw new RuntimeException("Did not find promotion id - " + theId);
        }

        return thePromotion;
    }
}
