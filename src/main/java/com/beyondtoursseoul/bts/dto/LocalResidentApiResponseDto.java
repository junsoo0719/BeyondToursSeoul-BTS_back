package com.beyondtoursseoul.bts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 서울시 내국인 생활인구 API (OA-14991) 응답 DTO
 * 서비스명: SPOP_LOCAL_RESD_DONG
 */
@Getter
@NoArgsConstructor
public class LocalResidentApiResponseDto {

    @JsonProperty("SPOP_LOCAL_RESD_DONG")
    private Body body;

    @Getter
    @NoArgsConstructor
    public static class Body {

        @JsonProperty("list_total_count")
        private int totalCount;

        @JsonProperty("RESULT")
        private Result result;

        @JsonProperty("row")
        private List<Row> rows;
    }

    @Getter
    @NoArgsConstructor
    public static class Result {

        @JsonProperty("CODE")
        private String code;

        @JsonProperty("MESSAGE")
        private String message;
    }

    @Getter
    @NoArgsConstructor
    public static class Row {

        @JsonProperty("STDR_DE_ID")
        private String date;           // 기준일 (YYYYMMDD)

        @JsonProperty("TMZON_PD_SE")
        private String timeZone;       // 시간대 코드 (00~23)

        @JsonProperty("ADSTRD_CODE_SE")
        private String dongCode;       // 행정동코드

        @JsonProperty("TOT_LVPOP_CO")
        private String totalPopulation; // 총생활인구수
    }
}
