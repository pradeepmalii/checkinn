package com.pradeepmali591.CheckInn.strategy.pricingStrategy;


import com.pradeepmali591.CheckInn.entity.Inventory;
import com.pradeepmali591.CheckInn.strategy.PricingStrategy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UrgencyPricingStrategy implements PricingStrategy {

    PricingStrategy wrapped;


    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);

        LocalDate today = LocalDate.now();
        if(!inventory.getDate().isBefore(today)
                && inventory.getDate().isBefore(today.plusDays(7))){

            price = price.multiply(BigDecimal.valueOf(1.5));
        }
        return price;
    }
}
