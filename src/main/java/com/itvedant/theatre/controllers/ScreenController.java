package com.itvedant.theatre.controllers;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itvedant.theatre.models.Screen;
import com.itvedant.theatre.models.Theatre;
import com.itvedant.theatre.repositories.ScreenRepository;
import com.itvedant.theatre.repositories.TheatreRepository;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {
	private final ScreenRepository screenRepository;
	private final TheatreRepository theatreRepository;

	public ScreenController(
			ScreenRepository screenRepository,
			TheatreRepository theatreRepository) {
		this.screenRepository = screenRepository;
		this.theatreRepository = theatreRepository;
	}

	@PostMapping
	public ResponseEntity<?> addScreen(@RequestBody Screen screen) {

		if (screen.getTheatre() == null
				|| screen.getTheatre().getId() == null) {
			return ResponseEntity.badRequest()
					.body("Theatre ID is required");
		}

		Optional<Theatre> existingTheatre =
				theatreRepository.findById(screen.getTheatre().getId());

		if (existingTheatre.isEmpty()) {
			return ResponseEntity.badRequest()
					.body("Theatre not found");
		}

		screen.setId(null);
		screen.setTheatre(existingTheatre.get());

		Screen savedScreen = screenRepository.save(screen);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(savedScreen);
	}

	@GetMapping
	public ResponseEntity<List<Screen>> getAllScreens() {
		return ResponseEntity.ok(screenRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Screen> getScreenById(
			@PathVariable Long id) {

		Optional<Screen> screen = screenRepository.findById(id);

		if (screen.isPresent()) {
			return ResponseEntity.ok(screen.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateScreen(
			@PathVariable Long id,
			@RequestBody Screen screenDetails) {

		Optional<Screen> existingScreen =
				screenRepository.findById(id);

		if (existingScreen.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Screen screen = existingScreen.get();

		screen.setScreenName(screenDetails.getScreenName());
		screen.setTotalSeats(screenDetails.getTotalSeats());

		if (screenDetails.getTheatre() != null
				&& screenDetails.getTheatre().getId() != null) {

			Optional<Theatre> theatre =
					theatreRepository.findById(
							screenDetails.getTheatre().getId());

			if (theatre.isEmpty()) {
				return ResponseEntity.badRequest()
						.body("Theatre not found");
			}

			screen.setTheatre(theatre.get());
		}

		Screen updatedScreen = screenRepository.save(screen);

		return ResponseEntity.ok(updatedScreen);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteScreen(
			@PathVariable Long id) {

		Optional<Screen> screen = screenRepository.findById(id);

		if (screen.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		if (!screen.get().getShows().isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Cannot delete screen because movie shows exist");
		}

		screenRepository.delete(screen.get());

		return ResponseEntity.ok("Screen deleted successfully");
	}
}