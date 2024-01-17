package com.example.hotelBooking.service;

import java.util.UUID;

import com.example.hotelBooking.entity.HotelBookingRequest;

import reactor.core.publisher.Mono;

/**
 * Service interface for hotel booking operations.
 */
public interface HotelBookingService {

    /**
     * Creates a new hotel booking based on the provided booking request.
     *
     * @param req The hotel booking request containing details for the new booking.
     * @return A Mono emitting the created HotelBookingRequest.
     */
    Mono<HotelBookingRequest> createBooking(HotelBookingRequest req);

    /**
     * Retrieves a specific hotel booking based on the provided booking ID.
     *
     * @param bookingId The unique identifier for the booking to be retrieved.
     * @return A Mono emitting the HotelBookingRequest matching the provided ID.
     */
    Mono<HotelBookingRequest> viewBooking(UUID bookingId);

    /**
     * Cancels a hotel booking based on the provided booking ID.
     *
     * @param bookingId The unique identifier for the booking to be canceled.
     * @return A Mono emitting a Boolean indicating the success of the cancellation.
     */
    Mono<Boolean> cancelBooking(UUID bookingId);
}
