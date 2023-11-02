package com.scaler.BookMyShow.repository;

import com.scaler.BookMyShow.models.MovieShow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShowRepository extends JpaRepository<MovieShow, Long> {
    @Override
    Optional<MovieShow> findById(Long showId);

    @Override
    MovieShow save(MovieShow show);
}
