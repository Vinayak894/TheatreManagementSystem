package com.itvedant.theatre.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.itvedant.theatre.models.MovieShow;

@Repository
public interface MovieShowRepository
        extends JpaRepository<MovieShow, Long> {
}
