package com.itvedant.theatre.controllers;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itvedant.theatre.models.Booking;
import com.itvedant.theatre.models.User;
import com.itvedant.theatre.repositories.BookingRepository;
import com.itvedant.theatre.repositories.UserRepository;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingRepository bookingRepository;
	private final UserRepository userRepository;

	public BookingController(
			BookingRepository bookingRepository,
			UserRepository userRepository) {
		this.bookingRepository = bookingRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/my")
	public List<Map<String, Object>> getMyBookings(
			Authentication authentication) {

		User user = userRepository
				.findByEmail(authentication.getName())
				.orElseThrow(() ->
				new IllegalArgumentException("User not found")
						);

		List<Booking> bookings =
				bookingRepository.findByUserId(user.getId());

		return bookings.stream()
				.map(booking -> Map.<String, Object>of(
						"bookingId", booking.getId(),
						"bookingTime", booking.getBookingTime(),
						"numberOfSeats", booking.getNumberOfSeats(),
						"totalAmount", booking.getTotalAmount(),
						"status", booking.getStatus(),
						"showId", booking.getMovieShow().getId()
						))
				.toList();
	}
}
