package com.itvedant.theatre.controllers;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

        Theatre savedTheatre = theatreRepository.save(theatre);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(savedTheatre);
    }

    @GetMapping
    public List<Theatre> getAllTheatres() {
        return theatreRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theatre> getTheatreById(
            @PathVariable Long id) {

        return theatreRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
