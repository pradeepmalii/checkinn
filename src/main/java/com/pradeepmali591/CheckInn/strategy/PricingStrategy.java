package com.pradeepmali591.CheckInn.strategy;

import com.pradeepmali591.CheckInn.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
