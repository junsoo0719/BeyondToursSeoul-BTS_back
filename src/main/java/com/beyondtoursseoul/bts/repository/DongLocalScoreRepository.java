package com.beyondtoursseoul.bts.repository;

import com.beyondtoursseoul.bts.domain.DongLocalScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DongLocalScoreRepository extends JpaRepository<DongLocalScore, Long> {

    boolean existsByDongCodeAndDateAndHour(String dongCode, LocalDate date, Integer hour);

    List<DongLocalScore> findByDate(LocalDate date);
}
