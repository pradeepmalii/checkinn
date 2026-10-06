package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.dto.booking.request.BookingRequest;
import com.pradeepmali591.CheckInn.dto.booking.request.GuestRequest;
import com.pradeepmali591.CheckInn.dto.booking.response.BookingResponse;
import com.stripe.model.Event;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface BookingService {

    BookingResponse initialiseBooking(BookingRequest request);

    BookingResponse addGuests(Long bookingId, List<GuestRequest> request);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    Object getBookingStatus(Long bookingId);

    List<BookingResponse> getAllBookingsByHotelId(Long hotelId);
}
