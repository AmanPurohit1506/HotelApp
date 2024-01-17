package com.example.hotelBooking;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.hotelBooking.controller.HotelController;
import com.example.hotelBooking.entity.HotelBookingRequest;
import com.example.hotelBooking.service.HotelBookingService;

import reactor.core.publisher.Mono;

/**
 * Unit tests for the HotelController class.
 */
@WebFluxTest(HotelController.class)
class HotelBookingApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private HotelBookingService hotelBookingService;

    /**
     * Test the successful creation of a hotel booking.
     * This test verifies that a booking is created successfully with the expected ID.
     */
    @Test
    public void testCreateBooking() {
        // Arrange
        HotelBookingRequest mockObject = getMockingHotelBookingObject();
        HotelBookingRequest mockRequest = getMockingHotelBookingObject();
        mockRequest.setGuestName(mockObject.getGuestName());
        mockRequest.setNumberOfGuests(mockObject.getNumberOfGuests());
        when(hotelBookingService.createBooking(mockRequest)).thenReturn(Mono.just(mockObject));

        // Act and Assert
        webTestClient.post().uri("/create").contentType(MediaType.APPLICATION_JSON).bodyValue(mockRequest).exchange()
                .expectStatus().isCreated().expectBody(String.class)
                .isEqualTo("Booking created successfully with ID: " + mockObject.getBookingId());
    }

    /**
     * Test the cancellation of a hotel booking.
     * This test verifies that a booking is canceled successfully.
     */
    @Test
    public void testCancelBooking() {
        // Arrange
        UUID bookingId = UUID.randomUUID();
        when(hotelBookingService.cancelBooking(bookingId)).thenReturn(Mono.just(true));

        // Act and Assert
        webTestClient.delete().uri("/cancel/{bookingId}", bookingId).exchange().expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Booking canceled successfully");
    }

    /**
     * Test the retrieval of a hotel booking by ID.
     * This test ensures that the viewBooking API returns the correct booking information.
     */
    @Test
    public void testViewBooking() {
        // Arrange
        HotelBookingRequest mockRequest = getMockingHotelBookingObject();
        UUID bookingId = mockRequest.getBookingId();
        when(hotelBookingService.viewBooking(bookingId)).thenReturn(Mono.just(mockRequest));

        // Act and Assert
        webTestClient.get().uri("/view/{bookingId}", bookingId).exchange().expectStatus().isOk()
                .expectBody(HotelBookingRequest.class).isEqualTo(mockRequest);
    }

    /**
     * Helper method to create a mock HotelBookingRequest object for testing.
     */
    public HotelBookingRequest getMockingHotelBookingObject() {
        UUID bookingId = UUID.randomUUID();
        HotelBookingRequest mockBooking = new HotelBookingRequest();
        mockBooking.setBookingId(bookingId);
        mockBooking.setGuestName("Anonymous");
        mockBooking.setNumberOfGuests(4);
        return mockBooking;
    }
}
