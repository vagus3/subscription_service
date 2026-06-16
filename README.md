# 🎯 SubTrack — 사용량 기반 구독 정리 서비스

> 방식: Thymeleaf + Spring Boot 통합형 (1번 방식) + 사용량 기반 구독 정리  
> 인원: 6명 (백엔드 3명 / 프론트엔드 3명)  
> 핵심 차별점: "얼마나 썼는지"를 직접 체크인하고, 히트맵 + 방치 경보 + 대체 추천으로 정리를 돕는다.

---

## 1. 기술 스택

| 영역 | 기술 |
|------|------|
| 백엔드 | Java 17, Spring Boot 3.x, Spring Data JPA, Spring Security, Spring Mail |
| 빌드 도구 | Gradle (Wrapper 사용 필수) |
| DB | MySQL 8.0 (로컬 개발 공용) |
| 프론트 | Thymeleaf 3.x, HTML5, CSS3, Vanilla JS |
| 에셋 빌드 | Node.js + Vite (`src/main/frontend/`) |
| 배포 | GitHub Actions CI/CD → EC2 / Codespaces |
| 실행 | `java -jar` 단일 JAR 배포 |

---

## 2. 프로젝트 폴더 구조
```
subscription-service/
├── .github/
│   └── workflows/
│       └── deploy.yml                 # CI/CD 워크플로우
├── bin/                               # 로컬 실행/보조 스크립트
├── docs/
│   └── Backend2.md                    # 백엔드 2 작업 문서
├── gradle/wrapper/                    # Gradle Wrapper 설정
├── scripts/
│   └── deploy.sh                      # 배포 쉘 스크립트
├── src/
│   ├── main/
│   │   ├── frontend/                  # Vite 원본 프론트 에셋
│   │   │   ├── main.js
│   │   │   ├── css/
│   │   │   │   ├── base.css
│   │   │   │   └── layout.css
│   │   │   └── styles/
│   │   │       ├── global.css
│   │   │       ├── utilties.css
│   │   │       ├── variable.css
│   │   │       └── components/
│   │   │           ├── button.css
│   │   │           ├── card.css
│   │   │           └── sidebar.css
│   │   ├── java/com/example/sub/
│   │   │   ├── SubscriptionApplication.java
│   │   │   ├── config/                # Security, Scheduler 설정
│   │   │   ├── controller/            # 화면/요청 Controller
│   │   │   ├── domain/entity/         # JPA Entity, Enum
│   │   │   ├── dto/                   # 요청/응답 DTO
│   │   │   ├── repository/            # Spring Data JPA Repository
│   │   │   ├── service/               # 비즈니스 로직
│   │   │   └── util/                  # 메일, 경보 등 공통 유틸
│   │   └── resources/
│   │       ├── application.yml        # 공통 설정
│   │       ├── application-local.yml  # 로컬 설정
│   │       ├── application-prod.yml   # 운영 설정
│   │       ├── db/
│   │       │   └── init.sql           # MySQL 초기화 스크립트
│   │       ├── static/assets/
│   │       │   └── main.js            # Vite 빌드 산출물
│   │       └── templates/
│   │           ├── index.html
│   │           ├── layout.html        # 공통 레이아웃
│   │           ├── login.html
│   │           ├── recommendation.html
│   │           ├── cancellation-log.html
│   │           ├── admin/             # 관리자 화면
│   │           ├── fragments/         # header, footer, nav
│   │           ├── member/            # 회원/내 구독 화면
│   │           ├── plan/              # 플랜 목록/상세 화면
│   │           └── subscribe/         # 구독 신청/결과 화면
│   └── test/
│       └── java/com/example/sub/      # 테스트 코드
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── package.json                       # Vite 빌드 스크립트
├── package-lock.json
├── vite.config.js
└── PROGRESS_BACKEND1.md
```

## 3. 팀원별 역할 상세

###  팀장 — 통합 & 배포 & 공통 백엔드 & 인사이트

| 담당 | 구체 산출물 |
|------|------------|
| 프로젝트 뼈대 | `build.gradle`, `settings.gradle`, `.gitignore` |
| DB 규약 | ERD 확정, 테이블/컬럼명 표준, 연관관계 설계 |
| 설정 | `application.yml`, `application-prod.yml` |
| 보안 | `SecurityConfig.java`, 비밀번호 암호화 Bean, login URL 규칙 |
| 배포 | `.github/workflows/deploy.yml`, `scripts/deploy.sh`, EC2 세팅 |
| 공통 유틸 | `MailUtil.java`, `SchedulerConfig.java` |
| 인사이트 | `UsageAnalyticsService.java`, `RecommendationService.java`, "대체 추천" 로직 |
| 해지 다이어리 | `CancellationLogController.java`, `CancellationLog.java` |
| 통합 리뷰 | 모든 Pull Request 리뷰 및 feature → develop → main 머지, 충돌 해결 |
| 화면 | `templates/admin/dashboard.html` (관리자 통계, 선택) |

###  백엔드 1 — 회원·인증·알림

| 담당 | 구체 산출물 |
|------|------------|
| Entity | `Member.java`, `Role.java` (Enum: ADMIN, USER) |
| Repository | `MemberRepository.java` |
| Service | `MemberService.java`, `CustomUserDetailsService.java` |
| Controller | `MemberController.java`, `LoginController.java` |
| 주요 URL | `GET/POST /join`, `/login`, `/member/mypage`, `/member/update` |
| 알림 | `ReminderService.java` (@Scheduled: 매일 오전 9시 이메일 발송) |
| 해지 태그 | `CancellationTag.java`, `CancellationLog.java` |
| Model 규약 | Controller에서 반드시 `member` key로 Member 객체를 담아 반환 |

###  백엔드 2 — 구독·체크인·집계·경보 (핵심 도메인)

| 담당 | 구체 산출물 |
|------|------------|
| Entity | `SubscriptionPlan.java`, `MemberSubscription.java`, `SubscriptionUsage.java` |
| Repository | `MemberSubscriptionRepository.java`, `SubscriptionUsageRepository.java` |
| Service | `SubscriptionService.java`, `CheckInService.java` |
| 집계 | `UsageStatisticsService.java` (주간/월간 COUNT, SUM) |
| 경보 | `AlertLevelResolver.java` → NORMAL / WARNING / DANGER / CRITICAL |
| Controller | `PlanController.java`, `SubscriptionController.java` |
| 주요 URL | `GET /`, `GET /plans/{id}`, `POST /subscribe`, `POST /checkin/{id}` |
| Model 규약 | `plans`, `plan`, `subscriptions`, `subscription`, `usageStats` |

###  프론트엔드 1 — UI 총괄·Asset·레이아웃

| 담당 | 구체 산출물 |
|------|------------|
| npm 프로젝트 관리 | `frontend/package.json`, `vite.config.js` |
| 빌드 책임 | `npm ci && npm run build` → `static/` 산출물 갱신 |
| 레이아웃 | `layout.html`, `fragments/header.html`, `footer.html`, `nav.html` |
| CSS 시스템 | 공통 클래스: `.btn`, `.card`, `.form-group`, `.container`, `.badge-*` |
| 히트맵 CSS | `.heatmap-grid` (30칸), `.heat-green`, `.heat-gray` 등 |
| 공통 JS | `main.js` (탭 타이틀 배지 갱신, 토스트 알림 기본 함수) |
| 규칙 | `layout.html` 외 `<html>`, `<head>`, `<body>` 직접 수정 금지 (팀 전체 공지) |

###  프론트엔드 2 — 회원 화면

| 담당 페이지 | 파일 경로 | 특징 |
|-----------|----------|------|
| 로그인 | `templates/login.html` | `th:if="${param.error}"`, Security form-login |
| 회원가입 | `templates/member/join.html` | `th:object="${memberForm}"`, `th:field` 바인딩 |
| 마이페이지 | `templates/member/mypage.html` | `th:text="${member.name}"`, 내 구독 요약 |
| 사용 패턴 요약 | `templates/member/usage-summary.html` | 이번 달 총 체크인 횟수, 방치 구독 수 |

> 모든 페이지는 `layout.html` 상속 필수

###  프론트엔드 3 — 구독 화면 (핵심 차별 UI)

| 담당 페이지 | 파일 경로 | 핵심 요소 |
|-----------|----------|----------|
| 메인/플랜 목록 | `templates/plan/list.html` | `th:each`, 체크인 토글, 히트맵 30칸 그리드, 경보 배지 |
| 플랜 상세 | `templates/plan/detail.html` | 플랜 정보, 확대 히트맵 |
| 구독 신청 폼 | `templates/subscribe/form.html` | 시작일, 약관 동의 |
| 신청 결과 | `templates/subscribe/result.html` | 성공/실패 메시지 |
| 내 구독 관리 | `templates/member/subscriptions.html` | 카드 리스트, 해지 버튼, 알림 설정 |
| 대체 추천 | `templates/recommendation.html` | "이 구독 대신 이걸 써보세요" 카드 |
| 해지 다이어리 | `templates/cancellation-log.html` | 과거 해지 목록, 태그 표시 |

> 히트맵 30칸: `<div class="heatmap-grid">` 안에 `th:each`로 `<div class="heat-*">` 30개 렌더링

---

## 4. Git 브랜치 전략

| 브랜치 | 담당 | 목적 |
|--------|------|------|
| `main` | 팀장 | 서버 배포용, 직접 push 금지, PR만 머지 |
| `develop` | 팀장 | 통합 테스트용, 직접 push 금지, PR만 머지 |
| `feature/common-ui` | 프론트 1 | 레이아웃, CSS, JS, npm 빌드 |
| `feature/member-auth` | 백엔드 1 + 프론트 2 | 회원가입, 로그인, 마이페이지 |
| `feature/subscription-core` | 백엔드 2 + 프론트 3 | 체크인, 히트맵, 경보, 추천 |
| `feature/insight-diary` | 팀장 | 대체 추천, 해지 다이어리, 관리자 |
| `feature/deploy-pipeline` | 팀장 | CI/CD, 배포 스크립트 |

> **feature/ui-system → develop 먼저 합친 뒤**, 나머지 feature는 develop 기반으로 따서 작업합니다.

---

## 5. Controller-View 데이터 흐름 표 (Model 규약)

| 화면 | Controller URL | Model key | Thymeleaf 사용 예시 |
|------|---------------|-----------|---------------------|
| 플랜 목록 | `GET /` | `plans` | `th:each="p : ${plans}"` |
| 플랜 상세 | `GET /plans/{id}` | `plan` | `th:text="${plan.name}"` |
| 회원가입 | `GET /join` | `memberForm` | `th:object="${memberForm}"` |
| 로그인 에러 | `/login?error` | param.error | `th:if="${param.error}"` |
| 마이페이지 | `GET /member/mypage` | `member` | `th:text="${member.email}"` |
| 내 구독 | `GET /subscriptions` | `subscriptions` | `th:each="s : ${subscriptions}"` |
| 체크인 결과 | `POST /checkin/{id}` | flashAttribute | `th:if="${successMessage}"` |

> **백엔드 1, 2는 이 표준 key 외에는 Model에 담지 않습니다.**

---

## 6. ERD 핵심 테이블

### `member`

```sql
id          BIGINT PK
email       VARCHAR(100) UNIQUE
password    VARCHAR(255)
name        VARCHAR(50)
role        VARCHAR(20)  -- ADMIN / USER
created_at  DATETIME

## 6. ERD 및 테이블 생성 SQL

> 아래 SQL은 MySQL 설치 후 root 계정으로 한 번만 실행하면 됩니다.  
> 또는 `mysql -u root -p` 접속 후 붙여넣기 하세요.

```sql
-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS subscription
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE subscription;

-- 사용자 생성
CREATE USER IF NOT EXISTS 'sub_user'@'localhost'
    IDENTIFIED BY 'sub_password';

GRANT ALL PRIVILEGES ON subscription.*
    TO 'sub_user'@'localhost';

FLUSH PRIVILEGES;

-- 1. member (회원)
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. subscription_plan (플랜 템플릿)
CREATE TABLE subscription_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    monthly_price INT NOT NULL,
    yearly_price INT NOT NULL,
    provider_url VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 3. member_subscription (사용자의 실제 구독)
CREATE TABLE member_subscription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    due_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    last_used_at DATE,
    alert_level VARCHAR(20) DEFAULT 'NORMAL',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (plan_id) REFERENCES subscription_plan(id)
);

-- 4. subscription_usage (체크인 기록)
CREATE TABLE subscription_usage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_subscription_id BIGINT NOT NULL,
    used_date DATE NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    duration_minutes INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_subscription_id) REFERENCES member_subscription(id)
);

-- 5. cancellation_log (해지 다이어리)
CREATE TABLE cancellation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    cancelled_at DATE NOT NULL,
    tags VARCHAR(255),
    memo TEXT,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (plan_id) REFERENCES subscription_plan(id)
);
