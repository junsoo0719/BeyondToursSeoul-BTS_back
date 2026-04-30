package com.beyondtoursseoul.bts.service;

import com.beyondtoursseoul.bts.service.AttractionApiService;
import com.beyondtoursseoul.bts.domain.Attraction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TourApiServiceTest {

    @Autowired
    private AttractionApiService attractionApiService;

    @Test
    void 서울_관광지_수집_성공() {
        List<Attraction> attractions = attractionApiService.fetchSeoulAttractions();

        System.out.println("수집된 관광지 수: " + attractions.size());
        if (!attractions.isEmpty()) {
            Attraction sample = attractions.get(0);
            System.out.printf("샘플 — name: %s, category: %s, address: %s, lng: %.6f, lat: %.6f%n",
                    sample.getName(),
                    sample.getCategory(),
                    sample.getAddress(),
                    sample.getGeom().getX(),
                    sample.getGeom().getY());
        }

        assertThat(attractions).isNotEmpty();
        assertThat(attractions).allMatch(a -> a.getSource().equals("TOUR_API"));
        assertThat(attractions).allMatch(a -> a.getGeom() != null);
        assertThat(attractions).allMatch(a -> a.getGeom().getX() != 0.0 && a.getGeom().getY() != 0.0);
    }
}
