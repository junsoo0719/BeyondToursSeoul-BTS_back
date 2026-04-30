package com.beyondtoursseoul.bts.repository;

import com.beyondtoursseoul.bts.domain.AttractionLocalScore;
import com.beyondtoursseoul.bts.domain.AttractionLocalScoreId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AttractionLocalScoreRepository extends JpaRepository<AttractionLocalScore, AttractionLocalScoreId> {

    boolean existsByIdDate(LocalDate date);
}
