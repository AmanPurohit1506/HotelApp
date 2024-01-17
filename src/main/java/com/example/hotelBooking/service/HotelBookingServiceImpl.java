package com.example.hotelBooking.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.hotelBooking.entity.HotelBookingRequest;
import com.example.hotelBooking.repository.HotelBookingRepository;
import reactor.core.publisher.Mono;

@Service
public class HotelBookingServiceImpl implements HotelBookingService {

	@Autowired
	HotelBookingRepository hotelBookingRepository;

	private static final String CACHE_KEY = "hotelBookings";

	/**
	 * Creates a new hotel booking.
	 *
	 * @param req The hotel booking details to be created.
	 * @return A Mono emitting the created HotelBookingRequest.
	 */

	@Override
	@CachePut(value = CACHE_KEY, key = "#result?.bookingId")
	public Mono<HotelBookingRequest> createBooking(HotelBookingRequest req) {
		req.setBookingId(UUID.randomUUID());
		return hotelBookingRepository.save(req).onErrorResume(throwable -> Mono.empty());
	}

	/**
	 * Retrieves a hotel booking based on the provided booking ID.
	 *
	 * @param bookingId The unique identifier for the booking to be retrieved.
	 * @return A Mono emitting the HotelBookingRequest corresponding to the provided
	 *         booking ID.
	 * @throws ResponseStatusException if the booking with the given ID is not
	 *                                 found.
	 */
	@Override
	@Cacheable(value = CACHE_KEY, key = "'#result?.bookingId'")
	public Mono<HotelBookingRequest> viewBooking(UUID bookingId) {
		return hotelBookingRepository.findById(bookingId)
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found")));
	}

	/**
	 * Cancels a hotel booking based on the provided booking ID.
	 *
	 * @param bookingId The unique identifier for the booking to be canceled.
	 * @return A Mono emitting a Boolean indicating the success of the cancellation.
	 * @throws ResponseStatusException if the booking with the given ID is not
	 *                                 found.
	 */

	@Override
	@CacheEvict(value = CACHE_KEY, key = "'#result?.bookingId'")
	public Mono<Boolean> cancelBooking(UUID bookingId) {
		return hotelBookingRepository.findById(bookingId)
				.flatMap(booking -> hotelBookingRepository.delete(booking).then(Mono.just(true)))
				.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found")));
	}
}
