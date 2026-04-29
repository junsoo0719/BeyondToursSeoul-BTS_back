package com.beyondtoursseoul.bts.service;

import com.beyondtoursseoul.bts.dto.TourApiResponseDto;
import com.beyondtoursseoul.bts.dto.TourApiResponseDto.Item;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TourApiService {

    private static final String BASE_URL = "https://apis.data.go.kr/B551011/KorService2/areaBasedList2";
    private static final String SEOUL_RIEGN_CD = "11";
    private static final String SOURCE = "TOUR_API";
    private static final int PAGE_SIZE = 1000;

    private static final GeometryFactory GF = new GeometryFactory(new PrecisionModel(), 4326);

    private final RestClient restClient;

    @Value("${PUBLIC_DATA_API_KEY}")
    private String apiKey;

    public TourApiService() {
        this.restClient = RestClient.create();
    }

    public List<com.beyondtoursseoul.bts.domain.Attraction> fetchSeoulAttractions() {
        List<com.beyondtoursseoul.bts.domain.Attraction> result = new ArrayList<>();
        int pageNo = 1;
        int totalCount = Integer.MAX_VALUE;

        while ((long) (pageNo - 1) * PAGE_SIZE < totalCount) {
            TourApiResponseDto response = restClient.get()
                    .uri(buildUrl(pageNo))
                    .retrieve()
                    .body(TourApiResponseDto.class);

            if (response == null
                    || response.getResponse() == null
                    || response.getResponse().getBody() == null) {
                log.warn("[TourAPI] 응답 없음 — pageNo: {}", pageNo);
                break;
            }

            TourApiResponseDto.Body body = response.getResponse().getBody();
            totalCount = body.getTotalCount();

            if (body.getItems() == null || body.getItems().getItem() == null) {
                log.warn("[TourAPI] 항목 없음 — pageNo: {}", pageNo);
                break;
            }

            List<Item> items = body.getItems().getItem();
            log.info("[TourAPI] pageNo={}, totalCount={}, 수신={}건", pageNo, totalCount, items.size());

            for (Item item : items) {
                toAttraction(item).ifPresent(result::add);
            }

            pageNo++;
        }

        log.info("[TourAPI] 서울 관광지 수집 완료: {}건", result.size());
        return result;
    }

    private java.util.Optional<com.beyondtoursseoul.bts.domain.Attraction> toAttraction(Item item) {
        if (item.getMapX() == null || item.getMapX().isBlank()
                || item.getMapY() == null || item.getMapY().isBlank()) {
            return java.util.Optional.empty();
        }

        try {
            double lng = Double.parseDouble(item.getMapX());
            double lat = Double.parseDouble(item.getMapY());

            if (lng == 0.0 || lat == 0.0) return java.util.Optional.empty();

            Point geom = GF.createPoint(new Coordinate(lng, lat));

            return java.util.Optional.of(
                    com.beyondtoursseoul.bts.domain.Attraction.builder()
                            .externalId(item.getContentId())
                            .name(item.getTitle())
                            .category(item.getContentTypeId())
                            .address(item.getAddr1())
                            .geom(geom)
                            .source(SOURCE)
                            .createdAt(OffsetDateTime.now())
                            .build()
            );
        } catch (NumberFormatException e) {
            log.warn("[TourAPI] 좌표 파싱 실패 contentId={}: {}", item.getContentId(), e.getMessage());
            return java.util.Optional.empty();
        }
    }

    private String buildUrl(int pageNo) {
        return BASE_URL
                + "?serviceKey=" + apiKey
                + "&numOfRows=" + PAGE_SIZE
                + "&pageNo=" + pageNo
                + "&MobileOS=ETC"
                + "&MobileApp=BTS"
                + "&_type=json"
                + "&arrange=A"
                + "&lDongRegnCd=" + SEOUL_RIEGN_CD;
    }
}
