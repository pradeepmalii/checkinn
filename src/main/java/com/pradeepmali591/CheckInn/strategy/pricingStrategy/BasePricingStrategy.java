package com.pradeepmali591.CheckInn.strategy.pricingStrategy;

import com.pradeepmali591.CheckInn.entity.Inventory;
import com.pradeepmali591.CheckInn.strategy.PricingStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public class BasePricingStrategy implements PricingStrategy {


    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
