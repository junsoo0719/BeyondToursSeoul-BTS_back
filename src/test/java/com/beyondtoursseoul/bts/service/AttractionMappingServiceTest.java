package com.beyondtoursseoul.bts.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AttractionMappingServiceTest {

    @Autowired
    private AttractionMappingService attractionMappingService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 매핑률이_99퍼센트_이상이다() {
        // InitialDataLoader가 앱 기동 시 이미 매핑 완료 → 결과 검증
        int total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM attraction", Integer.class);
        int nullCount = attractionMappingService.countNullDongCode();
        int mappedCount = total - nullCount;
        double mappingRate = (double) mappedCount / total * 100;

        System.out.printf("매핑률: %.2f%% (%d / %d, 미매핑: %d건)%n",
                mappingRate, mappedCount, total, nullCount);

        assertThat(mappingRate).isGreaterThanOrEqualTo(99.0);
    }

    @Test
    void mapDongCodes_재실행시_멱등성이_보장된다() {
        int beforeNull = attractionMappingService.countNullDongCode();

        // 이미 매핑된 상태에서 재실행해도 결과가 바뀌지 않아야 함
        attractionMappingService.mapDongCodes();

        int afterNull = attractionMappingService.countNullDongCode();
        System.out.printf("재실행 전 미매핑: %d건, 재실행 후 미매핑: %d건%n", beforeNull, afterNull);

        assertThat(afterNull).isEqualTo(beforeNull);
    }
}
