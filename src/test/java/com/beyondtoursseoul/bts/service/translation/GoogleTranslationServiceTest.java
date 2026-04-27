package com.beyondtoursseoul.bts.service.translation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoogleTranslationServiceTest {

    @Autowired
    private TranslationService translationService;

    @Test
    void 구글_번역_테스트() {
        String sourceText = "안녕하세요";
        String result = translationService.translate(sourceText, "ko", "en");

        System.out.println("결과: " + result);
        assertThat(result).containsIgnoringCase("Hello");
    }

    @Test
    void 구글_배치_번역_테스트() {
        List<String> sourceTexts = List.of("안녕하세요", "물품보관함", "감사합니다");

        List<String> result = translationService.translateBatch(sourceTexts, "ko", "en");

        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualTo("hello");
        assertThat(result.get(1)).isEqualTo("lockers");
        assertThat(result.get(2)).isEqualTo("thank you");

        System.out.println("결과: " + result.toString());

    }

}