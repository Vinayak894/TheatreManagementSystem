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
import com.itvedant.theatre.models.Theatre;
import com.itvedant.theatre.repositories.TheatreRepository;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {

    private final TheatreRepository theatreRepository;

    public TheatreController(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    @PostMapping
    public ResponseEntity<Theatre> addTheatre(
            @RequestBody Theatre theatre) {

        theatre.setId(null);

        Theatre savedTheatre = theatreRepository.save(theatre);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedTheatre);
    }

    @GetMapping
    public ResponseEntity<List<Theatre>> getAllTheatres() {
        return ResponseEntity.ok(theatreRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theatre> getTheatreById(
            @PathVariable Long id) {

        Optional<Theatre> theatre =
                theatreRepository.findById(id);

        if (theatre.isPresent()) {
            return ResponseEntity.ok(theatre.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theatre> updateTheatre(
            @PathVariable Long id,
            @RequestBody Theatre theatreDetails) {

        Optional<Theatre> existingTheatre =
                theatreRepository.findById(id);

        if (existingTheatre.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Theatre theatre = existingTheatre.get();

        theatre.setName(theatreDetails.getName());
        theatre.setCity(theatreDetails.getCity());

        Theatre updatedTheatre =
                theatreRepository.save(theatre);

        return ResponseEntity.ok(updatedTheatre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheatre(
            @PathVariable Long id) {

        if (!theatreRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        theatreRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}