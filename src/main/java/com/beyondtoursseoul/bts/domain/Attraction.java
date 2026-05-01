package com.beyondtoursseoul.bts.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;

@Entity
@Table(name = "attraction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", length = 100)
    private String externalId;

    @Column(length = 200)
    private String name;

    @Column(length = 50)
    private String category;

    private String address;

    @Column(columnDefinition = "GEOMETRY(Point, 4326)")
    private Point geom;

    @Column(name = "dong_code", length = 10)
    private String dongCode;

    @Column(length = 20)
    private String source;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    private String thumbnail;

    @Column(length = 10)
    private String cat1;

    @Column(length = 10)
    private String cat2;

    @Column(length = 10)
    private String cat3;

    @Column(columnDefinition = "TEXT")
    private String tel;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(name = "operating_hours", columnDefinition = "TEXT")
    private String operatingHours;

    @Column(name = "detail_fetched", nullable = false)
    private boolean detailFetched = false;

    @Builder
    public Attraction(String externalId, String name, String category, String address,
                      Point geom, String dongCode, String source, OffsetDateTime createdAt,
                      String thumbnail, String cat1, String cat2, String cat3,
                      String tel, String overview, String operatingHours) {
        this.externalId = externalId;
        this.name = name;
        this.category = category;
        this.address = address;
        this.geom = geom;
        this.dongCode = dongCode;
        this.source = source;
        this.createdAt = createdAt;
        this.thumbnail = thumbnail;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.tel = tel;
        this.overview = overview;
        this.operatingHours = operatingHours;
    }

    public void updateDongCode(String dongCode) {
        this.dongCode = dongCode;
    }

    public void updateDetail(String overview, String operatingHours, String tel) {
        this.overview = overview;
        this.operatingHours = operatingHours;
        if (this.tel == null && tel != null) this.tel = tel;
        this.detailFetched = true;
    }
}
