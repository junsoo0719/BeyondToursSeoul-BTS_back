package com.beyondtoursseoul.bts.service;

import com.beyondtoursseoul.bts.dto.LocalResidentApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class
LocalResidentApiService {

    private static final String SERVICE_NAME = "SPOP_LOCAL_RESD_DONG";
    private static final int PAGE_SIZE = 1000;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final RestClient restClient;

    @Value("${SEOUL_OPEN_API_KEY}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public LocalResidentApiService() {
        this.restClient = RestClient.create();
    }

    /**
     * 최근 7일 내 데이터가 존재하는 가장 최신 날짜 탐색 (1건만 조회해서 빠르게 확인)
     */
    public LocalDate findLatestAvailableDate() {
        for (int i = 1; i <= 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            String url = buildUrl(date.format(DATE_FORMAT), 1, 1);

            LocalResidentApiResponseDto response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(LocalResidentApiResponseDto.class);

            if (response != null && response.getBody() != null
                    && response.getBody().getTotalCount() > 0) {
                log.info("최신 생활인구 데이터 날짜: {}", date);
                return date;
            }
        }
        throw new IllegalStateException("최근 7일 내 내국인 생활인구 데이터를 찾을 수 없습니다.");
    }

    /**
     * 특정 날짜의 내국인 생활인구 전체 데이터 수집
     * 서울 행정동(11로 시작)만 필터링하여 반환
     */
    public List<LocalResidentApiResponseDto.Row> fetchByDate(LocalDate date) {
        String dateStr = date.format(DATE_FORMAT);
        List<LocalResidentApiResponseDto.Row> allRows = new ArrayList<>();

        int start = 1;
        int totalCount = Integer.MAX_VALUE;

        while (start <= totalCount) {
            int end = start + PAGE_SIZE - 1;
            String url = buildUrl(dateStr, start, end);

            log.info("내국인 생활인구 API 호출: {} ({}-{})", dateStr, start, end);

            LocalResidentApiResponseDto response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(LocalResidentApiResponseDto.class);

            if (response == null || response.getBody() == null || response.getBody().getRows() == null) {
                log.warn("응답 데이터 없음: {} ({}-{})", dateStr, start, end);
                break;
            }

            totalCount = response.getBody().getTotalCount();

            response.getBody().getRows().stream()
                    .filter(row -> row.getDongCode() != null && row.getDongCode().startsWith("11"))
                    .forEach(allRows::add);

            start += PAGE_SIZE;
        }

        log.info("내국인 생활인구 수집 완료: {}건 ({})", allRows.size(), dateStr);
        return allRows;
    }

    private String buildUrl(String date, int start, int end) {
        return String.format(
                "http://openapi.seoul.go.kr:8088/%s/json/%s/%d/%d/%s",
                apiKey, SERVICE_NAME, start, end, date
        );
    }
}
