package com.itvedant.theatre.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.itvedant.theatre.models.Theatre;
@Repository
public interface TheatreRepository
        extends JpaRepository<Theatre, Long> {
}