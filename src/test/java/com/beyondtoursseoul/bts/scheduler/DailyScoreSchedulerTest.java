package com.beyondtoursseoul.bts.scheduler;

import com.beyondtoursseoul.bts.repository.DongPopulationRawRepository;
import com.beyondtoursseoul.bts.service.LocalResidentApiService;
import com.beyondtoursseoul.bts.service.score.LocalScoreCalculateService;
import com.beyondtoursseoul.bts.service.score.PopulationCollectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyScoreSchedulerTest {

    @Mock LocalResidentApiService localResidentApiService;
    @Mock PopulationCollectService populationCollectService;
    @Mock LocalScoreCalculateService localScoreCalculateService;
    @Mock DongPopulationRawRepository rawRepository;

    @InjectMocks DailyScoreScheduler scheduler;

    @Test
    void 신규_날짜면_수집과_계산이_순서대로_실행된다() {
        LocalDate targetDate = LocalDate.of(2026, 4, 28);
        when(localResidentApiService.findLatestAvailableDate()).thenReturn(targetDate);
        when(rawRepository.existsByDate(targetDate)).thenReturn(false);

        scheduler.run();

        InOrder order = inOrder(populationCollectService, localScoreCalculateService);
        order.verify(populationCollectService).collect(targetDate);
        order.verify(localScoreCalculateService).calculateAndSave(targetDate);
    }

    @Test
    void 이미_수집된_날짜면_수집과_계산을_건너뛴다() {
        LocalDate targetDate = LocalDate.of(2026, 4, 28);
        when(localResidentApiService.findLatestAvailableDate()).thenReturn(targetDate);
        when(rawRepository.existsByDate(targetDate)).thenReturn(true);

        scheduler.run();

        verify(populationCollectService, never()).collect(any());
        verify(localScoreCalculateService, never()).calculateAndSave(any());
    }

    @Test
    void API_날짜_조회_실패시_앱이_죽지_않는다() {
        when(localResidentApiService.findLatestAvailableDate())
                .thenThrow(new IllegalStateException("최근 7일 내 데이터 없음"));

        assertDoesNotThrow(() -> scheduler.run());

        verify(populationCollectService, never()).collect(any());
        verify(localScoreCalculateService, never()).calculateAndSave(any());
    }

    @Test
    void 수집_실패시_계산을_실행하지_않고_앱이_죽지_않는다() {
        LocalDate targetDate = LocalDate.of(2026, 4, 28);
        when(localResidentApiService.findLatestAvailableDate()).thenReturn(targetDate);
        when(rawRepository.existsByDate(targetDate)).thenReturn(false);
        doThrow(new RuntimeException("API 오류")).when(populationCollectService).collect(targetDate);

        assertDoesNotThrow(() -> scheduler.run());

        verify(localScoreCalculateService, never()).calculateAndSave(any());
    }
}
