package com.itvedant.theatre.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.itvedant.theatre.models.Booking;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);
}
