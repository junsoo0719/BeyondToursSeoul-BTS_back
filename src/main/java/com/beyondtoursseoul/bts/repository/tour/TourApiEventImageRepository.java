package com.beyondtoursseoul.bts.repository.tour;

import com.beyondtoursseoul.bts.domain.tour.TourApiEvent;
import com.beyondtoursseoul.bts.domain.tour.TourApiEventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourApiEventImageRepository extends JpaRepository<TourApiEventImage, Long> {
    void deleteByEvent(TourApiEvent event);
    List<TourApiEventImage> findByEvent(TourApiEvent event);
}
