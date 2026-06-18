# 🎨 Frontend 1 - UI & Asset Progress

## ✅ 완료 항목 (가이드 명세 준수)

### 1. npm 프로젝트 및 빌드 시스템
- [x] `package.json`, `vite.config.js` 설정 완료
- [x] Vite 빌드 파이프라인 구축 (`npm run build` -> `static/assets/`)
- [x] `.gitignore` 빌드 산출물 제외 규칙 적용

### 2. 공통 레이아웃 및 프래그먼트
- [x] `layout.html`: 전체 구조, 모바일 하단 내비게이션, 공통 모달 뼈대 구축
- [x] `header.html`: 검색창 및 사용자 프로필 영역
- [x] `nav.html`: 사이드바 메뉴 및 새 구독 추가 버튼
- [x] `footer.html`: 서비스 정보 및 링크

### 3. CSS 디자인 시스템 (가이드 명세 클래스)
- [x] `.btn`, `.card`, `.form-group` 기본 스타일 구현
- [x] `.container`: 공통 컨테이너 레이아웃
- [x] `.badge-*`: 상태 표시용 배지 시스템 (success, warning, danger)
- [x] **히트맵 시스템**: `.heatmap-grid`, `.heat-green`, `.heat-gray`, 방치 경보 애니메이션

### 4. 공통 JS 유틸리티 (`main.js`)
- [x] 토스트 알림 (`showToast`)
- [x] 탭 타이틀 배지 갱신 (`updateTabBadge`)
- [x] 금액/날짜 포맷팅 및 퍼센트 계산 유틸리티
- [x] 히트맵 렌더링 로직 및 자동 스캔 기능

## 🛠 최근 업데이트 (2026-06-18)
- **브랜드 정체성 확립:** 프로젝트 전체 브랜드명을 `SubTrack`으로 통일 (Fragments 및 회원 관련 전 페이지 교정)
- **UI 시스템 통합 및 정화:** 타 담당자 페이지의 가이드 위반 스타일(인라인 스타일, 내부 style 태그)을 공통 디자인 시스템으로 통합 및 정화 완료
- **빌드 파이프라인 최적화:** `vite.config.js` 경로 수정을 통해 빌드 자산과 `layout.html` 간의 정적 리소스 연결 무결성 확보
- **협업 기반 마련:** 해지 다이어리, 대체 추천, 구독 신청 등 미구현 페이지의 레이아웃 상속 구조 및 UI 뼈대 구축 완료

## 🗓 향후 계획
- 팀원들의 신규 UI 컴포넌트 요청 대응 및 코드 리뷰
- 최종 배포 전 브라우저 호환성 전수 점검
