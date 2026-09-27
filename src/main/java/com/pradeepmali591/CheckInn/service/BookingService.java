package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.dto.booking.request.BookingRequest;
import com.pradeepmali591.CheckInn.dto.booking.request.GuestRequest;
import com.pradeepmali591.CheckInn.dto.booking.response.BookingResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface BookingService {

    BookingResponse initialiseBooking(BookingRequest request);

    BookingResponse addGuests(Long bookingId, List<GuestRequest> request);
}
