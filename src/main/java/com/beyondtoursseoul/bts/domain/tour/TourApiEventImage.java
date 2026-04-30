package com.beyondtoursseoul.bts.domain.tour;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tour_api_event_image")
public class TourApiEventImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private TourApiEvent event;

    @Column(name = "origin_img_url", length = 1000)
    private String originImgUrl; // 원본 이미지 URL

    @Column(name = "small_img_url", length = 1000)
    private String smallImgUrl; // 썸네일 이미지 URL

    @Column(name = "copyright_type")
    private String copyrightType; // 저작권 유형 (cpyrhtDivCd)
}
