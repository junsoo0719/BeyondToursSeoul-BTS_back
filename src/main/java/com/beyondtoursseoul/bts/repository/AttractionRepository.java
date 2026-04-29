package com.beyondtoursseoul.bts.repository;

import com.beyondtoursseoul.bts.domain.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {
}
