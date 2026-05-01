package com.beyondtoursseoul.bts.controller;

import com.beyondtoursseoul.bts.dto.attraction.AttractionDetailResponse;
import com.beyondtoursseoul.bts.dto.attraction.AttractionSummaryResponse;
import com.beyondtoursseoul.bts.service.AttractionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attractions")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionQueryService attractionQueryService;

    @GetMapping
    public ResponseEntity<List<AttractionSummaryResponse>> getList(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "afternoon") String timeSlot) {
        return ResponseEntity.ok(attractionQueryService.getList(date, timeSlot));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttractionDetailResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(attractionQueryService.getDetail(id));
    }
}
