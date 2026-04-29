package com.beyondtoursseoul.bts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiResponseDto {

    @JsonProperty("response")
    private Response response;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {

        @JsonProperty("body")
        private Body body;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {

        @JsonProperty("items")
        private Items items;

        @JsonProperty("totalCount")
        private int totalCount;

        @JsonProperty("numOfRows")
        private int numOfRows;

        @JsonProperty("pageNo")
        private int pageNo;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {

        @JsonProperty("item")
        private List<Item> item;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        @JsonProperty("contentid")
        private String contentId;

        @JsonProperty("title")
        private String title;

        @JsonProperty("contenttypeid")
        private String contentTypeId;

        @JsonProperty("addr1")
        private String addr1;

        @JsonProperty("mapx")
        private String mapX;   // 경도 (longitude)

        @JsonProperty("mapy")
        private String mapY;   // 위도 (latitude)
    }
}
