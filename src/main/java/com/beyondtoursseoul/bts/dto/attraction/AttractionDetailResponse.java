package com.beyondtoursseoul.bts.dto.attraction;

import com.beyondtoursseoul.bts.domain.Attraction;
import com.beyondtoursseoul.bts.domain.AttractionLocalScore;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
public class AttractionDetailResponse {

    private final Long id;
    private final String name;
    private final String thumbnail;
    private final String cat1Name;
    private final String cat2Name;
    private final String cat3Name;
    private final String address;
    private final double lng;
    private final double lat;
    private final String tel;
    private final String overview;
    private final String operatingHours;
    private final Map<String, BigDecimal> scores;

    public AttractionDetailResponse(Attraction attraction,
                                    String cat1Name, String cat2Name, String cat3Name,
                                    Map<String, BigDecimal> scores) {
        this.id = attraction.getId();
        this.name = attraction.getName();
        this.thumbnail = attraction.getThumbnail();
        this.cat1Name = cat1Name;
        this.cat2Name = cat2Name;
        this.cat3Name = cat3Name;
        this.address = attraction.getAddress();
        this.lng = attraction.getGeom().getX();
        this.lat = attraction.getGeom().getY();
        this.tel = attraction.getTel();
        this.overview = attraction.getOverview();
        this.operatingHours = attraction.getOperatingHours();
        this.scores = scores;
    }
}
