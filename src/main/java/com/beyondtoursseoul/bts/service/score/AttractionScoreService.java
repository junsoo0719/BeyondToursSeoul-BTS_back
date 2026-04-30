package com.beyondtoursseoul.bts.service.score;

import com.beyondtoursseoul.bts.repository.AttractionLocalScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionScoreService {

    private final JdbcTemplate jdbcTemplate;
    private final AttractionLocalScoreRepository repository;

    private static final List<String> ALL_TIME_SLOTS = Arrays.stream(TimeSlot.values())
            .map(TimeSlot::getCode)
            .toList();

    public void calculateAndSave(LocalDate date) {
        if (repository.existsByIdDate(date)) {
            log.info("[AttractionScore] {} 이미 계산됨 — 스킵", date);
            return;
        }

        int total = 0;
        for (String timeSlot : ALL_TIME_SLOTS) {
            int count = insertForTimeSlot(date, timeSlot);
            total += count;
            log.info("[AttractionScore] {} {} — {}건 저장", date, timeSlot, count);
        }

        log.info("[AttractionScore] 완료: {} 총 {}건", date, total);
    }

    private int insertForTimeSlot(LocalDate date, String timeSlot) {
        return jdbcTemplate.update("""
                INSERT INTO attraction_local_score (attraction_id, date, time_slot, score)
                SELECT a.id, ?, ?, s.score
                FROM attraction a
                JOIN dong_local_score s ON s.dong_code = a.dong_code
                WHERE s.date = ? AND s.time_slot = ?
                  AND a.dong_code IS NOT NULL
                ON CONFLICT (attraction_id, date, time_slot) DO UPDATE SET score = EXCLUDED.score
                """, date, timeSlot, date, timeSlot);
    }

    // Approach C: contentTypeId 기준 대표 시간대
    public static TimeSlot primaryTimeSlot(String contentTypeId) {
        if (contentTypeId == null) return TimeSlot.AFTERNOON;
        return switch (contentTypeId) {
            case "39" -> TimeSlot.EVENING;    // 음식점
            case "15" -> TimeSlot.EVENING;    // 행사/공연/축제
            case "28" -> TimeSlot.MORNING;    // 레포츠
            case "32" -> TimeSlot.MORNING;    // 숙박
            default   -> TimeSlot.AFTERNOON;  // 관광지(12), 문화시설(14), 쇼핑(38) 등
        };
    }
}
