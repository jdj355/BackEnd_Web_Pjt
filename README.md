# 🥗 냠냠코치 — 식단 분석 기반 개인 맞춤형 건강 코칭 서비스

> SSAFY 16기 Java Track · 관통 프로젝트 **Web(Back)** (4회차)

먹은 음식을 음식 영양성분 DB에서 검색해 식단으로 기록하면, 탄수화물·단백질·지방의 비율과 영양 점수를 분석해 보여주는 웹 서비스입니다.
Servlet + JSP 기반의 MVC 구조로 구현했습니다.

<br>

## 📌 목차
1. [개발 환경](#-개발-환경)
2. [실행 방법](#-실행-방법)
3. [기능 목록](#-기능-목록)
4. [프로젝트 구조](#-프로젝트-구조)
5. [계층별 역할 (MVC)](#-계층별-역할-mvc)
6. [요청 흐름](#-요청-흐름)
7. [URL 목록](#-url-목록)
8. [화면 구성](#-화면-구성)
9. [팀원 및 역할](#-팀원-및-역할)

<br>

## 🛠 개발 환경

| 구분 | 사용 기술 |
|---|---|
| Language | Java 17+ |
| Server | Apache Tomcat 11 (Jakarta Servlet 6.1) |
| View | JSP, EL, JSTL 3.0 |
| UI | Bootstrap 5.3, Pretendard 폰트 |
| Data | JSON 파일 저장소 (Gson 2.10.1) |
| IDE | STS (Spring Tool Suite) |
| 협업 | Git, GitHub |

<br>

## ▶ 실행 방법

1. **프로젝트 가져오기**
   ```bash
   git clone https://github.com/jdj355/BackEnd_Web_Pjt.git
   ```
   STS에서 `File → Import → Existing Projects into Workspace`로 가져옵니다.

2. **Tomcat 연결**
   - `Window → Preferences → Server → Runtime Environments`에서 Apache Tomcat v11.0을 등록합니다.
   - 프로젝트 우클릭 → `Properties → Targeted Runtimes`에서 Apache Tomcat v11.0을 체크합니다.

3. **라이브러리 확인** (`src/main/webapp/WEB-INF/lib`)
   - `gson-2.10.1.jar`
   - `jakarta.servlet.jsp.jstl-api-3.0.x.jar`
   - `jakarta.servlet.jsp.jstl-3.0.x.jar`

4. **데이터 파일 확인** (`src/main/webapp/WEB-INF/data`)
   - `foods.json` : 음식 영양성분 DB (`name`, `calories`, `protein`, `carbs`, `fat`)
   - `diet-data.json` : 저장된 식단 (처음에는 `[]`)

5. **서버 실행**
   - 프로젝트 우클릭 → `Run As → Run on Server` → Tomcat v11.0
   - 브라우저에서 `http://localhost:8080/{컨텍스트 경로}/` 접속
   - STS Console에 `N개의 음식을 불러왔습니다.`가 출력되면 데이터가 정상적으로 로드된 것입니다.

<br>

## ✅ 기능 목록

### 기본 기능 (필수)

| 번호 | 분류 | 기능 | 설명 | 상태 |
|---|---|---|---|---|
| F401 | 식단 | 식단 작성 | 음식 DB에서 음식을 검색·선택해 날짜, 식사 유형과 함께 식단을 등록 | ✅ |
| F402 | 식단 | 식단 조회 | 전체 식단 목록 조회, 식단 ID로 상세 조회 | ✅ |
| F403 | 식단 | 식단 수정 | 날짜, 식사 유형, 음식 목록 수정 | ✅ |
| F404 | 식단 | 식단 삭제 | 식단 ID로 삭제 (삭제 전 확인 창) | ✅ |
| F405 | 식단 | 식단 분석 | 총 칼로리·영양소 합계, 열량 기준 탄·단·지 비율, 영양 점수와 코멘트 제공 | ✅ |
| F406 | 회원 | 회원 가입 | 아이디, 비밀번호, 이름, 키·몸무게·질환 등 프로필 등록 | 🔧 진행 중 |
| F407 | 회원 | 회원 조회 | 로그인한 사용자의 정보 조회 | 🔧 진행 중 |
| F408 | 회원 | 회원 수정 | 본인 회원 정보 수정 | 🔧 진행 중 |
| F409 | 회원 | 회원 탈퇴 | 회원 삭제 또는 비활성화 | 🔧 진행 중 |
| F410 | 회원 | 로그인/로그아웃 | 세션 기반 로그인 상태 관리 | 🔧 진행 중 |

### 추가 기능

| 번호 | 분류 | 기능 | 상태 |
|---|---|---|---|
| F411 | 회원 | 팔로우 / 팔로잉 | ⬜ 예정 |
| F412 | 챌린지 | 챌린지 정보 관리 | ⬜ 예정 |
| F413 | 챌린지 | 챌린지 사용자 참여 | ⬜ 예정 |
| F414 | 커뮤니티 | 게시판 | ⬜ 예정 |
| F415 | 커뮤니티 | 댓글 | ⬜ 예정 |

### 심화 기능

| 번호 | 분류 | 기능 | 상태 |
|---|---|---|---|
| F416 | AI | 생성형 AI를 활용한 식단 분석 | ⬜ 예정 |
| F417 | AI | 생성형 AI를 활용한 운동 코칭 | ⬜ 예정 |

### 식단 분석(F405) 기준

- **열량 기준 비율**: 탄수화물·단백질 1g = 4kcal, 지방 1g = 9kcal로 환산해 각 영양소가 차지하는 비율을 계산합니다.
- **영양 점수**: 권장 비율 `탄수화물 55 : 단백질 20 : 지방 25`와의 차이만큼 100점에서 감점합니다.
- **코멘트**: 점수와 비율에 따라 "단백질이 부족해요", "지방 비율이 높아요" 등의 피드백을 보여줍니다.

> 현재 점수 기준은 일반 권장 비율을 사용합니다. 회원 프로필(키, 몸무게, 질환)을 반영한 맞춤 분석은 이후 개선 예정입니다.

<br>

## 📁 프로젝트 구조

```
src/main/java
├── com.ssafy.diet.controller
│   └── DietController.java        # 식단 요청 처리 (/diet)
├── com.ssafy.diet.dto
│   ├── Diet.java                  # 식단 (id, 날짜, 식사 유형, 음식 목록)
│   ├── Food.java                  # 음식 (이름, 칼로리, 단백질, 탄수화물, 지방)
│   └── User.java                  # 회원
├── com.ssafy.diet.exception
│   └── LoginException.java        # 로그인 실패 예외
├── com.ssafy.diet.repository
│   ├── UserRepository.java        # 회원 데이터 접근
│   └── UserRepositoryImpl.java
├── com.ssafy.diet.service
│   ├── UserService.java           # 회원 비즈니스 로직
│   └── UserServiceImpl.java
└── diet
    ├── DietManager.java           # 식단 비즈니스 로직 + 데이터 접근
    └── DietManagerImpl.java       # JSON 파일 저장소, 식단 분석, 음식 검색

src/main/webapp
├── index.jsp                      # 시작 페이지
└── WEB-INF
    ├── web.xml
    ├── data
    │   ├── diet-data.json         # 식단 저장 파일
    │   └── foods.json             # 음식 영양성분 DB
    ├── lib                        # gson, jstl
    └── views                      # WEB-INF 아래에 두어 Controller를 거쳐야만 접근 가능
        ├── diet
        │   ├── _style.jspf        # 식단 화면 공통 스타일
        │   ├── list.jsp           # 식단 목록
        │   ├── detail.jsp         # 식단 상세 + 분석
        │   └── form.jsp           # 식단 작성 / 수정 (공용)
        └── user
            ├── login.jsp
            ├── signup.jsp
            └── mypage.jsp
```

<br>

## 🧩 계층별 역할 (MVC)

| 계층 | 클래스 / 파일 | 역할 |
|---|---|---|
| **Controller** | `DietController` | HTTP 요청의 `action` 파라미터에 따라 처리할 기능을 나누고, 입력값을 검증한 뒤 Service를 호출합니다. 결과를 request에 담아 JSP로 forward하거나, 등록·수정·삭제 후에는 redirect합니다. |
| **Service** | `DietManager(Impl)`, `UserService(Impl)` | 식단 분석(F405), 음식 검색, ID 생성, 중복 확인 등 비즈니스 로직을 처리합니다. |
| **Repository** | `DietManagerImpl`의 파일 입출력, `UserRepository(Impl)` | JSON 파일에 데이터를 저장·조회·수정·삭제합니다. 서버 시작 시 파일을 읽어 메모리(Map)에 올리고, 변경이 생기면 파일에 다시 저장합니다. |
| **DTO** | `Diet`, `Food`, `User` | 계층 간에 주고받는 데이터를 담습니다. |
| **View** | `views/**/*.jsp` | EL과 JSTL로 Controller가 전달한 데이터를 출력합니다. 스크립틀릿(Java 코드)은 사용하지 않습니다. |

### 설계 포인트

- **Front Controller 패턴**: 기능마다 서블릿을 만들지 않고 `/diet` 하나에서 `action` 값으로 분기합니다. (`list`, `detail`, `writeForm`, `write`, `updateForm`, `update`, `delete`)
- **PRG 패턴 (Post-Redirect-Get)**: 등록·수정·삭제(POST) 후 redirect해서, 새로고침해도 같은 요청이 다시 전송되지 않게 했습니다.
- **JSP를 WEB-INF 아래에 배치**: 브라우저가 JSP 주소로 직접 접근할 수 없고, 반드시 Controller를 거치도록 했습니다.
- **데이터 경로 처리**: Tomcat은 실행 위치가 프로젝트 폴더가 아니어서 상대 경로(`db/...`)로는 파일을 찾지 못합니다. `DietController.init()`에서 `getServletContext().getRealPath("/WEB-INF/data")`로 실제 경로를 구해 전달합니다.
- **동시성**: 여러 사용자의 요청이 동시에 들어와도 데이터가 꼬이지 않도록 저장소 메서드에 `synchronized`를 적용했습니다.

<br>

## 🔄 요청 흐름

식단 작성(F401)을 예로 들면 다음과 같습니다.

```
[form.jsp] 음식 검색 → 체크 → 저장 버튼
      │  POST /diet?action=write  (date, type, foodName[])
      ▼
[DietController.write()]
      │  1. 파라미터 → Diet 객체 생성 (음식 이름으로 음식 DB에서 Food 조회)
      │  2. 입력 검증 (날짜 형식, 식사 유형, 음식 1개 이상)
      │     └ 실패 시 errorMsg와 함께 form.jsp로 forward (입력값 유지)
      ▼
[DietManagerImpl.add()]
      │  Map에 저장 → diet-data.json 파일에 반영
      ▼
redirect → GET /diet?action=detail&id={id}
      ▼
[DietController.detail()] → manager.analyze(diet)
      ▼
[detail.jsp] 식단 정보 + 분석 결과 출력
```

<br>

## 🔗 URL 목록

| 기능 | Method | URL | 화면 | 요구사항 |
|---|---|---|---|---|
| 식단 목록 | GET | `/diet` 또는 `/diet?action=list` | `list.jsp` | F402 |
| 식단 상세 + 분석 | GET | `/diet?action=detail&id={id}` | `detail.jsp` | F402, F405 |
| 식단 작성 화면 (+음식 검색) | GET | `/diet?action=writeForm&keyword={검색어}` | `form.jsp` | F401 |
| 식단 등록 | POST | `/diet` (`action=write`) | → 상세로 redirect | F401 |
| 식단 수정 화면 (+음식 검색) | GET | `/diet?action=updateForm&id={id}` | `form.jsp` | F403 |
| 식단 수정 | POST | `/diet` (`action=update`) | → 상세로 redirect | F403 |
| 식단 삭제 | POST | `/diet` (`action=delete`) | → 목록으로 redirect | F404 |

> 회원 기능 URL은 회원 Controller 구현 후 추가 예정입니다.

<br>

## 🖥 화면 구성

### 1. 식단 목록 (`list.jsp`)
- 식단을 카드 형태로 보여줍니다. 카드마다 날짜, 식사 유형 배지(아침·점심·저녁·간식별 색상), 음식 태그, 총 칼로리를 표시합니다.
- 카드를 누르면 상세 화면으로 이동합니다.
- 기록이 없으면 안내 문구와 "첫 식단 기록하기" 버튼을 보여줍니다.

### 2. 식단 상세 + 분석 (`detail.jsp`)
- **영양 점수**: 100점 만점 점수를 원형 그래프로 보여주고, 한 줄 코멘트를 함께 표시합니다.
- **영양소 구성**: 칼로리, 탄수화물, 단백질, 지방의 합계와 비율을 카드로 보여줍니다.
- **비율 비교 막대**: 내 식단의 탄·단·지 비율과 권장 비율을 나란히 비교합니다.
- **먹은 음식 표**: 음식별 영양 성분과 합계를 보여줍니다.
- 수정, 삭제 버튼 (삭제 전 확인 창)

### 3. 식단 작성 / 수정 (`form.jsp`)
작성과 수정에 같은 화면을 사용합니다. 식단 ID가 있으면 수정 화면이 됩니다.
1. **언제 드셨나요?** 날짜와 식사 유형(버튼형 선택)을 입력합니다.
2. **선택한 음식**: 지금까지 고른 음식과 칼로리 합계를 보여줍니다. 체크를 해제하면 목록에서 빠집니다.
3. **음식 검색**: 음식 이름으로 DB를 검색하고, 결과 카드를 클릭해 선택합니다.
- 검색할 때 같은 폼을 GET으로 다시 보내기 때문에, 입력 중이던 날짜·식사 유형·선택한 음식이 유지됩니다.
- 입력값에 문제가 있으면 상단에 오류 메시지를 보여주고 입력값은 그대로 유지합니다.
- 저장 버튼은 화면 하단에 고정되어 스크롤 위치와 상관없이 누를 수 있습니다.

<br>

## 👥 팀원 및 역할

| 이름 | 담당 |
|---|---|
| 전동재 | 식단 기능 (F401~F405) — DietController, DietManager, 식단 화면(JSP) |
| 김태현 | 회원 기능 (F406~F410) — UserService, UserRepository, 회원 화면(JSP) |
