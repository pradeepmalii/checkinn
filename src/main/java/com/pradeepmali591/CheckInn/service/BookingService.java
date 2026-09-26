package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.dto.booking.request.BookingRequest;
import com.pradeepmali591.CheckInn.dto.booking.response.BookingResponse;
import org.jspecify.annotations.Nullable;

public interface BookingService {

    BookingResponse initialiseBooking(BookingRequest request);
}
