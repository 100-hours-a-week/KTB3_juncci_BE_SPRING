# KTB3_juncci_BE_SPRING 


## 프로젝트 소개 — logoRism
<table>
  <tr>
    <td width="180">
      <!-- 로고 이미지 -->
      <img width="512" height="512" alt="Icon" src="https://github.com/user-attachments/assets/6ff77802-e2bf-4eb9-a93f-a992837a9269" />
    </td>
    <td>
      <strong>logoRism</strong>은 <em>log</em>와 <em>algorithm</em>의 합성어로,  
      “개발자가 남기는 기록(log)”과 “사고의 과정(algorithm)을 함께 보존한다”는 의미를 담고 있습니다.  
      <br><br>
      단순한 코드 저장소가 아니라, <strong>개발자의 생각과 흐름까지 기록되는 공간</strong>을 구현하고자 하는 프로젝트입니다.  
      개발자의 삶에서 <em>기록된 코드</em>는 가장 확실한 존재 증명이며,  
      그 기록이 쌓여 결국 <strong>개발자의 이름(brand)</strong>이 된다는 메시지를 담고 있습니다.
    </td>
  </tr>
</table>

---

## 개발 인원 및 기간

* 개발 인원: 프론트엔드 / 백엔드 1인 개발 

* 개발 기간: 2025.09 ~ 2025.12

---
## 사용 기술 및 Tools
### Backend
<p>
  <img src="https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white&style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5.6-6DB33F?logo=springboot&logoColor=white&style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring_Web-6DB33F?logo=spring&logoColor=white&style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring_Validation-6DB33F?logo=spring&logoColor=white&style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring_Data_JPA-59666C?logo=spring&logoColor=white&style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?logo=springsecurity&logoColor=white&style=for-the-badge" />
  <img src="https://img.shields.io/badge/JWT-0.11.5-000000?logo=jsonwebtokens&logoColor=white&style=for-the-badge" />
</p>

### 🗄 Database
<p>
  <img src="https://img.shields.io/badge/MySQL-8.x-4479A1?logo=mysql&logoColor=white&style=for-the-badge" />
</p>

### 📘 API Documentation
<p>
  <img src="https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black&style=for-the-badge" />
</p>


---

## 주요 기능

* 사용자 회원가입 / 로그인 / 이메일 중복 확인
* 게시글 CRUD
* 댓글 CRUD
* 좋아요 등록 / 취소
* Swagger 기반 API 문서 자동 생성
* 예외 통합 처리 및 응답 포맷 표준화

---

## 프로젝트 구조 (Architecture Overview)

- Spring Boot MVC 패턴을 기반으로 **5계층 아키텍처**로 구성되어 있습니다.
- 각 계층은 단일 책임 원칙(SRP)과 의존성 역전 원칙(DIP)을 준수하도록 설계되었습니다.

| WEEK 04                                                                                                                               | WEEK 05                                                                                                                               |
| ------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| <img width="4710" height="2062" alt="WEEK04" src="https://github.com/user-attachments/assets/e9230aeb-3a7e-4a2c-8db2-05ae5f94ccc9" /> | <img width="6549" height="1880" alt="WEEK05" src="https://github.com/user-attachments/assets/5ab57361-2c16-44ac-8736-c2850075deaf" /> |

| WEEK 06 | WEEK 07 |
| ------- | -------- |
| <img width="5936" height="1804" alt="WEEK06" src="https://github.com/user-attachments/assets/d6053fca-39d7-4c84-a413-08f98c153a3a" /> | <img width="4152" height="1522" alt="WEEK07" src="https://github.com/user-attachments/assets/f6abe3a0-c76e-4082-9530-2d8e93a56d3f" /> |


**최종**
<img width="6791" height="3382" alt="image" src="https://github.com/user-attachments/assets/c642e3c0-fe32-452a-917d-e2313c6a3a98" />


---

## 1️⃣ Controller Layer

**✔ 역할**
클라이언트 요청을 처리하고, Service 호출 후 `ResponseFactory`를 사용해
표준 응답(`ApiResponseDto`)을 반환합니다.

**✔ 주요 클래스**

* `UserController`
* `PostController`
* `CommentController`
* `LikeController`

---

## 2️⃣ Service Layer

**✔ 역할**
비즈니스 로직을 수행하는 핵심 계층으로,
데이터 처리·검증·트랜잭션 관리 등을 담당합니다.
`AuthService`는 Authorization 헤더 기반 JWT 사용자 식별 기능을 제공합니다.

**✔ 주요 클래스**

* `UserService`
* `PostService`
* `CommentService`
* `LikeService`
* `AuthService`

---

## 3️⃣ Repository Layer

**✔ 역할**
데이터베이스에 접근하여 CRUD 및 조회 기능을 제공합니다.
Spring Data JPA 기반으로 구현되어 SQL 작성 없이 DB 조작이 가능합니다.

**✔ 주요 클래스**

* `UserRepository`
* `PostRepository`
* `CommentRepository`
* `LikeRepository`

---

## 4️⃣ Model Layer

### Entity Layer

**✔ 역할**
DB 테이블과 매핑되는 도메인 객체를 정의합니다.

**✔ 주요 클래스**

* `User`
* `Post`
* `Comment`
* `Like`

### Enum Layer

**✔ 역할**
도메인 상태 값 정의.

**✔ 주요 클래스**

* `UserStatus`

---

## 5️⃣ DTO Layer

### Request DTO

클라이언트에서 전달되는 입력 데이터 구조를 담당합니다.

**✔ 주요 클래스**

* `SignupRequest`, `LoginRequest`
* `PostCreateRequest`, `PostUpdateRequest`
* `CommentCreateRequest`, `CommentUpdateRequest`
* `UserUpdateRequest`, `UserPasswordUpdateRequest`

### Response DTO

API 응답 데이터 모델을 정의합니다.

**✔ 주요 클래스**

* `UserResponse`, `SignupResponse`
* `PostListResponse`, `PostDetailResponse`
* `CommentResponse`, `CommentListResponse`
* `LikeActionResponse`, `LikeCountResponse`
* `CheckEmailResponse`

---

## 6️⃣ Security Layer

**✔ 역할**
JWT 기반 인증/인가 처리를 담당하며 Spring Security와 연동됩니다.

**✔ 주요 클래스**

* `JwtAuthenticationFilter` — JWT 토큰 검증 필터
* `TokenProvider` — JWT 생성·검증
* `CustomUserDetailsService` — 사용자 정보 로드
* `CustomAccessDeniedHandler` — 권한 부족 에러 처리
* `CustomAuthenticationEntryPoint` — 인증 실패 처리
* `SecurityUtil` — SecurityContext 유틸

---

## 7️⃣ Config Layer

**✔ 역할**
애플리케이션 동작 환경 및 공통 설정을 구성합니다.

**✔ 주요 클래스**

* `SecurityConfig` — Spring Security 설정
* `PasswordConfig` — PasswordEncoder 설정
* `WebConfig` — CORS, 인코딩, 인터셉터 설정
* `SwaggerConfig` — Swagger(OpenAPI) UI 설정

---

## 8️⃣ Exception Layer

**✔ 역할**
비즈니스/시스템 예외를 통합적으로 처리하여 일관된 응답 구조를 제공합니다.

**✔ 주요 클래스**

* `BusinessException`
* `ErrorCode`
* `ErrorResponse`
* `GlobalExceptionHandler`

---

## API 예시

| Method   | Endpoint                               | Description |
| -------- | -------------------------------------- | ----------- |
| `POST`   | `/users`                               | 회원가입        |
| `POST`   | `/users/auth/login`                          | 로그인         |
| `GET`    | `/users/check-email`                   | 이메일 중복 확인   |
| `POST`   | `/posts`                               | 게시글 작성      |
| `GET`    | `/posts`                               | 게시글 목록 조회   |
| `GET`    | `/posts/{postId}`                      | 게시글 상세 조회   |
| `POST`   | `/posts/{postId}/comments`             | 댓글 작성       |
| `DELETE` | `/posts/{postId}/comments/{commentId}` | 댓글 삭제       |
| `POST`   | `/posts/{postId}/likes`                | 좋아요 추가      |
| `DELETE` | `/posts/{postId}/likes`                | 좋아요 취소      |

---

## Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## 디렉토리 구조

```
src/main/java/com/example/WEEK04
 ├── common/              # 공통 응답 객체(ApiResponseDto) 및 ResponseFactory
 ├── config/              # Security, Swagger(OpenAPI), Web 설정 클래스
 ├── controller/          # REST API 진입점 (User, Post, Comment, Like)
 ├── exception/           # 예외 정의(BusinessException), ErrorCode, 전역 핸들러
 ├── model/
 │   ├── dto/
 │   │   ├── request/     # 요청 데이터(Request DTO)
 │   │   └── response/    # 응답 데이터(Response DTO)
 │   ├── entity/          # JPA 엔티티(User, Post, Comment, Like)
 │   └── enums/           # 도메인 Enum(UserStatus 등)
 ├── repository/          # Spring Data JPA Repository 인터페이스
 ├── security/            # JWT 인증/인가, UserDetailsService, TokenProvider
 └── service/             # 비즈니스 로직 계층(Service 클래스)
```

---

 (업데이트: 2025.12.05)
