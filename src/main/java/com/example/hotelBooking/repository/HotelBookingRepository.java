package com.example.hotelBooking.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.example.hotelBooking.entity.HotelBookingRequest;

/**
 * Repository interface for managing hotel booking entities.
 */
@Repository
public interface HotelBookingRepository extends ReactiveCassandraRepository<HotelBookingRequest, UUID> {

}
