package com.beyondtoursseoul.bts.service;

import com.beyondtoursseoul.bts.repository.DongBoundaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DongBoundaryImportService {

    private final DongBoundaryRepository dongBoundaryRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public void importFromGeoJson(InputStream inputStream) throws Exception {
        JsonNode root = objectMapper.readTree(inputStream);
        JsonNode features = root.get("features");

        List<Object[]> batchArgs = new ArrayList<>();

        for (JsonNode feature : features) {
            JsonNode properties = feature.get("properties");
            String admCd = properties.get("adm_cd").asText();

            // 서울만 적재 (코드 앞 2자리 = 11)
            if (!admCd.startsWith("11")) continue;

            // 10자리 → 8자리 통일 (생활인구 API 기준)
            String dongCode = admCd.length() > 8 ? admCd.substring(0, 8) : admCd;
            String dongName = properties.get("adm_nm").asText();
            String geomJson = objectMapper.writeValueAsString(feature.get("geometry"));

            batchArgs.add(new Object[]{dongCode, dongName, geomJson});
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO dong_boundary (dong_code, dong_name, geom) VALUES (?, ?, ST_GeomFromGeoJSON(?))",
                batchArgs
        );

        log.info("행정동 경계 데이터 {}건 저장 완료", batchArgs.size());
    }
}
