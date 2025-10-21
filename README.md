# KTB3_juncci_BE_SPRING

과제 프로젝트로,
**회원가입 / 로그인 / 게시판 기능**을 중심으로 한 RESTful API 서버를 구현했습니다.

> **Last Updated:** 2025.10.21

---

## 주요 기능

* 사용자 회원가입 / 로그인
* 게시글 작성 · 수정 · 삭제 · 조회
* 댓글 작성 · 삭제 · 조회
* 게시글 좋아요 등록 / 취소
* Swagger 기반 API 문서 자동 생성

---

## 프로젝트 구조 (Architecture Overview)

Spring MVC 기반의 **5계층 아키텍처**를 따르며,
각 계층은 **단일 책임 원칙(SRP)** 과 **의존성 역전 원칙(DIP)** 을 준수하도록 리팩토링되었습니다.

| WEEK 04                                                                                                                               | WEEK 05                                                                                                                               |
| ------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| <img width="4710" height="2062" alt="WEEK04" src="https://github.com/user-attachments/assets/e9230aeb-3a7e-4a2c-8db2-05ae5f94ccc9" /> | <img width="6549" height="1880" alt="WEEK05" src="https://github.com/user-attachments/assets/5ab57361-2c16-44ac-8736-c2850075deaf" /> |

WEEK 06
<img width="5936" height="1804" alt="Mermaid Chart - Create complex, visual diagrams with text -2025-10-21-100902" src="https://github.com/user-attachments/assets/d6053fca-39d7-4c84-a413-08f98c153a3a" />


---

## 1️⃣ Controller Layer

**역할:**
클라이언트 요청을 수신하고 `Service`를 호출한 뒤,
`ResponseFactory`를 통해 표준화된 응답을 반환합니다.

**주요 클래스**

* `UserController` — 회원가입, 로그인, 이메일 중복 확인
* `PostController` — 게시글 CRUD
* `CommentController` — 댓글 작성, 삭제, 조회
* `LikeController` — 게시글 좋아요/취소

**리팩토링 포인트**

* `ResponseEntity` 직접 생성 제거 → `ResponseFactory`로 통일
* Swagger `@Operation`, `@ApiResponse` 적용으로 문서 자동화
* Controller는 “요청 처리 + Service 호출”만 담당 (SRP 준수)

---

## 2️⃣ Service Layer

**역할:**
핵심 비즈니스 로직 수행

**주요 클래스**

* `UserService`
* `PostService`
* `CommentService`
* `LikeService`
* `AuthService` (신규) — 인증 전담 모듈

**리팩토링 포인트**

* `AuthService`를 도입해 토큰 파싱 및 검증 로직을 단일화
* 서비스 간 중복된 인증 로직 제거 → 단일 책임 원칙(SRP) 강화
* 예외 상황은 `BusinessException`을 통해 일관 처리
* Repository에 직접 접근하며 CRUD 수행

---

## 3️⃣ Repository Layer

**역할:**
데이터의 영속성 관리

**주요 클래스**

* `DummyUserRepository`
* `DummyPostRepository`
* `DummyCommentRepository`
* `DummyLikeRepository`


---

## 4️⃣ Exception Layer

**역할:**
애플리케이션 전역의 예외를 통합 관리

**주요 클래스**

* `BusinessException`
* `ErrorCode`
* `GlobalExceptionHandler`
* `ErrorResponse`

**리팩토링 포인트**

* Controller 단위 예외 처리 제거 → `@RestControllerAdvice`로 중앙 집중화
* `ErrorCode` Enum을 통한 코드·메시지 일원화
* Swagger `@ApiResponse`로 예외 문서 자동 반영
* JSON 파싱 오류 등 일반 예외도 일관된 `ErrorResponse` 포맷으로 반환

---

## 5️⃣ Response Layer (신규)

**역할:**
API 응답 구조 표준화

**주요 클래스**

* `ApiResponseDto<T>`
* `ResponseFactory`

**리팩토링 포인트**

* 모든 Controller에서 `ResponseEntity` 생성 중복 제거
* 응답 포맷(`code`, `message`, `data`) 통일
* 성공(`ok`), 생성(`created`), 삭제(`noContent`) 응답 전용 메서드 제공
* Swagger 문서 자동 반영 (`@Schema`, `@ExampleObject` 등 활용)

```java
@Schema(description = "공통 API 응답 포맷")
public class ApiResponseDto<T> {
    private String code;
    private String message;
    private T data;
}
```

```java
@Component
public class ResponseFactory {
    public <T> ResponseEntity<ApiResponseDto<T>> ok(T data) { ... }
    public <T> ResponseEntity<ApiResponseDto<T>> created(T data) { ... }
    public ResponseEntity<ApiResponseDto<Void>> noContent() { ... }
}
```

---

## 6️⃣ Model Layer

**역할:**
요청/응답 DTO와 엔티티 관리

**주요 구성**

* **Entity**: `User`, `Post`, `Comment`
* **Request DTO**: `SignupRequest`, `LoginRequest`, `PostCreateRequest`, `CommentCreateRequest`, etc.
* **Response DTO**: `PostListResponse`, `CommentResponse`, `ErrorResponse`, etc.

**리팩토링 포인트**

* DTO에 `@Valid`, `@Email`, `@NotBlank` 등을 적용하여 컨트롤러 진입 전 자동 검증
* 수동 Validator 제거 (Validation Layer 삭제)
* 날짜 필드는 `LocalDateTime` 유지 → DTO 변환 시 문자열 포맷팅

---
