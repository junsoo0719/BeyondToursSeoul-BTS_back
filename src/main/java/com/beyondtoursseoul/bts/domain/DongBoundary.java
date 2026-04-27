package com.beyondtoursseoul.bts.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dong_boundary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DongBoundary {

    @Id
    @Column(name = "dong_code", length = 10)
    private String dongCode;

    @Column(name = "dong_name", length = 50)
    private String dongName;

    // geom 컬럼은 JdbcTemplate + ST_GeomFromGeoJSON으로 직접 관리
    @Transient
    private Object geom;

    @Builder
    public DongBoundary(String dongCode, String dongName) {
        this.dongCode = dongCode;
        this.dongName = dongName;
    }
}
