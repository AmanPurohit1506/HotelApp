package com.example.hotelBooking.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.hotelBooking.service.HotelBookingService;

import com.example.hotelBooking.entity.HotelBookingRequest;

import reactor.core.publisher.Mono;

/**
 * Controller class for managing hotel bookings.
 */
@Component
@RestController
public class HotelController {

	@Autowired
	HotelBookingService hotelBookingService;

	/**
	 * Endpoint to create a new hotel booking.
	 *
	 * @param req The request body containing booking details.
	 * @return ResponseEntity indicating the success or failure of the booking
	 *         creation.
	 */
	@PostMapping("/create")
	public Mono<ResponseEntity<String>> createBooking(@RequestBody HotelBookingRequest req) {
		return hotelBookingService.createBooking(req)
				.map(savedBooking -> new ResponseEntity<>(
						"Booking created successfully with ID: " + savedBooking.getBookingId(), HttpStatus.CREATED))
				.defaultIfEmpty(new ResponseEntity<>("Failed to create booking", HttpStatus.INTERNAL_SERVER_ERROR));
	}

	/**
	 * Endpoint to retrieve details of a specific hotel booking.
	 *
	 * @param bookingId The unique identifier of the booking.
	 * @return Mono containing the hotel booking details or an error response.
	 */
	@GetMapping("/view/{bookingId}")
	public Mono<HotelBookingRequest> viewBooking(@PathVariable UUID bookingId) {
	    return hotelBookingService.viewBooking(bookingId)
	            .onErrorResume(ResponseStatusException.class, e -> {
	                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	                	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
	                }
	                return Mono.error(e);
	            });
	}

	/**
	 * Endpoint to cancel a hotel booking.
	 *
	 * @param bookingId The unique identifier of the booking to be canceled.
	 * @return ResponseEntity indicating the success or failure of the booking
	 *         cancellation.
	 */
	@DeleteMapping("/cancel/{bookingId}")
	public Mono<ResponseEntity<String>> cancelBooking(@PathVariable UUID bookingId) {
		return hotelBookingService.cancelBooking(bookingId)
				.map(result -> new ResponseEntity<>("Booking canceled successfully", HttpStatus.OK))
				.onErrorResume(ResponseStatusException.class, ex -> {
					if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
						return Mono.just(new ResponseEntity<>("Booking not found", HttpStatus.NOT_FOUND));
					} else {
						return Mono.just(
								new ResponseEntity<>("Failed to cancel booking", HttpStatus.INTERNAL_SERVER_ERROR));
					}
				});
	}

}
