package com.beyondtoursseoul.bts;

import com.beyondtoursseoul.bts.dto.ForeignResidentApiResponseDto;
import com.beyondtoursseoul.bts.dto.LocalResidentApiResponseDto;
import com.beyondtoursseoul.bts.service.ForeignResidentApiService;
import com.beyondtoursseoul.bts.service.LocalResidentApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class BtsApplicationTests {

    @Autowired
    private LocalResidentApiService localResidentApiService;

    @Autowired
    private ForeignResidentApiService foreignResidentApiService;

    @Test
    void contextLoads() {
    }

    @Test
    void 내국인_생활인구_API_호출_테스트() {
        LocalDate latestDate = localResidentApiService.findLatestAvailableDate();
        List<LocalResidentApiResponseDto.Row> rows = localResidentApiService.fetchByDate(latestDate);

        System.out.println("수집된 행 수: " + rows.size());

        if (!rows.isEmpty()) {
            LocalResidentApiResponseDto.Row sample = rows.get(0);
            System.out.println("샘플 데이터:");
            System.out.println("  행정동코드: " + sample.getDongCode());
            System.out.println("  날짜: " + sample.getDate());
            System.out.println("  시간대: " + sample.getTimeZone());
            System.out.println("  총생활인구: " + sample.getTotalPopulation());
        }
    }

    @Test
    void 외국인_생활인구_API_호출_테스트() {
        LocalDate latestDate = foreignResidentApiService.findLatestAvailableDate();
        List<ForeignResidentApiResponseDto.Row> rows = foreignResidentApiService.fetchByDate(latestDate);

        System.out.println("수집된 행 수: " + rows.size());

        if (!rows.isEmpty()) {
            ForeignResidentApiResponseDto.Row sample = rows.get(0);
            System.out.println("샘플 데이터:");
            System.out.println("  행정동코드: " + sample.getDongCode());
            System.out.println("  날짜: " + sample.getDate());
            System.out.println("  시간대: " + sample.getTimeZone());
            System.out.println("  총생활인구: " + sample.getTotalPopulation());
        }
    }
}
