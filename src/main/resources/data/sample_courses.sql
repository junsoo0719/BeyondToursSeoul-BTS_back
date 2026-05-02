-- ====================================================================
-- AI 추천 여행 코스 샘플 데이터 (초기화 및 삽입)
-- ====================================================================

-- 기존 샘플 데이터가 있다면 삭제 (ID 관계상 자식 테이블부터 삭제)
DELETE FROM user_saved_courses WHERE course_id IN (SELECT id FROM tour_courses WHERE title IN ('서울의 과거와 현재: 고궁 밤산책', '예술과 휴식이 있는 하루', 'MZ세대 힙플레이스 탐방기'));
DELETE FROM tour_course_items WHERE course_id IN (SELECT id FROM tour_courses WHERE title IN ('서울의 과거와 현재: 고궁 밤산책', '예술과 휴식이 있는 하루', 'MZ세대 힙플레이스 탐방기'));
DELETE FROM tour_course_translations WHERE course_id IN (SELECT id FROM tour_courses WHERE title IN ('서울의 과거와 현재: 고궁 밤산책', '예술과 휴식이 있는 하루', 'MZ세대 힙플레이스 탐방기'));
DELETE FROM tour_courses WHERE title IN ('서울의 과거와 현재: 고궁 밤산책', '예술과 휴식이 있는 하루', 'MZ세대 힙플레이스 탐방기');

-- 1. 추천 코스 마스터 데이터 삽입 (tour_courses)
INSERT INTO tour_courses (title, hashtags, featured_image, created_at)
VALUES 
('서울의 과거와 현재: 고궁 밤산책', '#고궁 #야경 #전통 #데이트', 'https://tong.visitkorea.or.kr/cms/resource/21/2616021_image2_1.jpg', NOW()),
('예술과 휴식이 있는 하루', '#미술관 #전시 #공원 #힐링', 'https://tong.visitkorea.or.kr/cms/resource/23/2678623_image2_1.jpg', NOW()),
('MZ세대 힙플레이스 탐방기', '#팝업스토어 #트렌디 #인생샷 #서울숲', 'https://tong.visitkorea.or.kr/cms/resource/46/2800046_image2_1.jpg', NOW());

-- 2. 다국어 정보 삽입 (영어 버전)
INSERT INTO tour_course_translations (course_id, language, title, hashtags)
SELECT id, 'ENG', 'Past and Present of Seoul: Palace Night Walk', '#Palace #NightView #Tradition #Date' FROM tour_courses WHERE title = '서울의 과거와 현재: 고궁 밤산책'
ON CONFLICT (course_id, language) DO NOTHING;

INSERT INTO tour_course_translations (course_id, language, title, hashtags)
SELECT id, 'ENG', 'A Day of Art and Relaxation', '#Museum #Exhibition #Park #Healing' FROM tour_courses WHERE title = '예술과 휴식이 있는 하루'
ON CONFLICT (course_id, language) DO NOTHING;

INSERT INTO tour_course_translations (course_id, language, title, hashtags)
SELECT id, 'ENG', 'Hip Place Tour for MZ Generation', '#PopUpStore #Trendy #SeoulForest' FROM tour_courses WHERE title = 'MZ세대 힙플레이스 탐방기'
ON CONFLICT (course_id, language) DO NOTHING;

-- 3. 코스별 상세 아이템(관광지/행사) 결합 삽입
-- [코스 1: 고궁 밤산책]
INSERT INTO tour_course_items (course_id, item_type, attraction_id, event_id, sequence_order, ai_comment)
VALUES 
(
  (SELECT id FROM tour_courses WHERE title = '서울의 과거와 현재: 고궁 밤산책' LIMIT 1), 
  'ATTRACTION', 
  (SELECT id FROM attraction WHERE name LIKE '%경복궁%' LIMIT 1), 
  NULL, 1, '서울의 상징인 경복궁에서 조선시대의 웅장함을 느껴보세요.'
),
(
  (SELECT id FROM tour_courses WHERE title = '서울의 과거와 현재: 고궁 밤산책' LIMIT 1), 
  'EVENT', 
  NULL, 
  (SELECT content_id FROM tour_api_event LIMIT 1), 
  2, '현재 고궁 근처에서 열리는 특별한 문화 행사에 참여하여 추억을 만드세요.'
);

-- [코스 2: 예술과 휴식]
INSERT INTO tour_course_items (course_id, item_type, attraction_id, event_id, sequence_order, ai_comment)
VALUES 
(
  (SELECT id FROM tour_courses WHERE title = '예술과 휴식이 있는 하루' LIMIT 1), 
  'ATTRACTION', 
  (SELECT id FROM attraction WHERE name LIKE '%국립중앙박물관%' LIMIT 1), 
  NULL, 1, '한국의 역사와 예술을 한눈에 볼 수 있는 세계적인 규모의 박물관입니다.'
),
(
  (SELECT id FROM tour_courses WHERE title = '예술과 휴식이 있는 하루' LIMIT 1), 
  'ATTRACTION', 
  (SELECT id FROM attraction WHERE name LIKE '%덕수궁%' LIMIT 1), 
  NULL, 2, '고즈넉한 돌담길을 따라 걸으며 여유로운 오후를 즐겨보세요.'
);

-- [코스 3: 힙플레이스]
INSERT INTO tour_course_items (course_id, item_type, attraction_id, event_id, sequence_order, ai_comment)
VALUES 
(
  (SELECT id FROM tour_courses WHERE title = 'MZ세대 힙플레이스 탐방기' LIMIT 1), 
  'ATTRACTION', 
  (SELECT id FROM attraction WHERE name LIKE '%서울숲%' LIMIT 1), 
  NULL, 1, '도심 속 거대한 숲에서 피크닉과 산책을 즐기기 좋습니다.'
),
(
  (SELECT id FROM tour_courses WHERE title = 'MZ세대 힙플레이스 탐방기' LIMIT 1), 
  'EVENT', 
  NULL, 
  (SELECT content_id FROM tour_api_event OFFSET 1 LIMIT 1), 
  2, '주변에서 열리는 힙한 팝업 행사나 전시를 확인해보세요!'
);
