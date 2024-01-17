package com.example.hotelBooking.entity;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Represents a hotel booking request.
 *
 * This entity is used to store information about hotel bookings.
 */
@Data
@Table
public class HotelBookingRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Unique identifier for the booking.
	 */
	@Id
	@PrimaryKey
	private UUID bookingId;

	/**
	 * The name of the guest making the booking.
	 */
	@JsonProperty("guestName")
	private String guestName;

	/**
	 * The number of guests included in the booking.
	 */
	@JsonProperty("numOfGuests")
	private int numberOfGuests;
}
