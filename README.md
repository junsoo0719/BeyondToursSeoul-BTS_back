# BeyondToursSeoul-BTS_back

## 초기 데이터 설정

### 행정동 GeoJSON
행정동 경계 데이터는 용량 문제로 git에서 제외되어 있습니다. 아래 순서로 직접 준비해주세요.

1. [vuski/admdongkor](https://github.com/vuski/admdongkor) 에서 최신 `HangJeongDong_ver*.geojson` 다운로드
2. 파일명을 `seoul_dong.geojson` 으로 변경
3. `src/main/resources/geojson/seoul_dong.geojson` 경로에 저장
4. 앱 실행 시 서울 행정동 데이터가 자동으로 DB에 적재됩니다
