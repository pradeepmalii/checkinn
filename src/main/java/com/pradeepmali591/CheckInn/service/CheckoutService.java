package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.entity.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);
}
