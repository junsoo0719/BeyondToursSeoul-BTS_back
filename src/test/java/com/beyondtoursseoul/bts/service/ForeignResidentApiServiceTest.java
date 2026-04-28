package com.beyondtoursseoul.bts.service;

import com.beyondtoursseoul.bts.dto.ForeignResidentApiResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ForeignResidentApiServiceTest {

    @Autowired
    private ForeignResidentApiService foreignResidentApiService;

    @Test
    void 최신_데이터_날짜_탐색() {
        LocalDate date = foreignResidentApiService.findLatestAvailableDate();

        System.out.println("최신 외국인 생활인구 데이터 날짜: " + date);
        assertThat(date).isNotNull();
        assertThat(date).isBefore(LocalDate.now());
    }

    @Test
    void 특정_날짜_외국인_생활인구_수집() {
        LocalDate date = foreignResidentApiService.findLatestAvailableDate();
        List<ForeignResidentApiResponseDto.Row> rows = foreignResidentApiService.fetchByDate(date);

        System.out.println("수집된 행 수: " + rows.size());
        if (!rows.isEmpty()) {
            ForeignResidentApiResponseDto.Row sample = rows.get(0);
            System.out.println("샘플 행 - 날짜: " + sample.getDate()
                    + ", 시간대: " + sample.getTimeZone()
                    + ", 행정동코드: " + sample.getDongCode()
                    + ", 생활인구: " + sample.getTotalPopulation());
        }

        assertThat(rows).isNotEmpty();
        assertThat(rows).allMatch(row -> row.getDongCode().startsWith("11"));
    }
}
