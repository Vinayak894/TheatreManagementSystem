package com.itvedant.theatre.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.itvedant.theatre.models.Screen;

@Repository
public interface ScreenRepository
        extends JpaRepository<Screen, Long> {
}