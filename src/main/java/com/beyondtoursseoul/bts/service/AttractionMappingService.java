package com.beyondtoursseoul.bts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionMappingService {

    private static final int FALLBACK_RADIUS_METERS = 500;

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void mapDongCodes() {
        int total = countTotal();
        log.info("[매핑] 시작 — 전체 관광지: {}건", total);

        int contained = applyContains();
        log.info("[매핑] ST_Contains 매핑: {}건", contained);

        int fallback = applyDWithinFallback();
        log.info("[매핑] ST_DWithin({}m) fallback 매핑: {}건", FALLBACK_RADIUS_METERS, fallback);

        int remaining = countNullDongCode();
        log.info("[매핑] 완료 — 매핑률: {}/{} (미매핑: {}건)",
                total - remaining, total, remaining);
    }

    // ST_Contains: 관광지 좌표가 행정동 폴리곤 내부에 포함되는 경우
    private int applyContains() {
        return jdbcTemplate.update("""
                UPDATE attraction a
                SET dong_code = d.dong_code
                FROM dong_boundary d
                WHERE ST_Contains(d.geom, a.geom)
                  AND a.dong_code IS NULL
                """);
    }

    // ST_DWithin fallback: 경계선 위 또는 좌표 오차로 누락된 항목을 가장 가까운 행정동으로 매핑
    private int applyDWithinFallback() {
        return jdbcTemplate.update("""
                UPDATE attraction a
                SET dong_code = (
                    SELECT d.dong_code
                    FROM dong_boundary d
                    WHERE ST_DWithin(a.geom::geography, d.geom::geography, ?)
                    ORDER BY ST_Distance(a.geom::geography, d.geom::geography)
                    LIMIT 1
                )
                WHERE a.dong_code IS NULL
                  AND EXISTS (
                      SELECT 1 FROM dong_boundary d
                      WHERE ST_DWithin(a.geom::geography, d.geom::geography, ?)
                  )
                """, FALLBACK_RADIUS_METERS, FALLBACK_RADIUS_METERS);
    }

    public int countNullDongCode() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM attraction WHERE dong_code IS NULL", Integer.class);
        return count != null ? count : 0;
    }

    private int countTotal() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM attraction", Integer.class);
        return count != null ? count : 0;
    }
}
