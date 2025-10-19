# KTB3_juncci_BE_SPRING

과제 프로젝트로,
**회원가입 / 로그인 / 게시판 기능**을 중심으로 한 RESTful API 서버를 구현하였습니다.

(업데이트: 2025.10.19)

---

## 주요 기능

* 사용자 회원가입 / 로그인
* 게시글 작성 · 수정 · 삭제 · 조회
* 댓글 작성 · 삭제 · 조회
* 게시글 좋아요 등록 / 취소
* Swagger 기반 API 문서 자동 생성

---

## 프로젝트 구조 (Architecture Overview)

Spring MVC 기반 **5계층 아키텍처**를 따르며,
각 계층은 **단일 책임 원칙(SRP)** 과 **의존성 역전 원칙(DIP)** 을 준수하도록 리팩토링되었습니다.

(WEEK 04)
<img width="4710" height="2062" alt="WEEK04" src="https://github.com/user-attachments/assets/e9230aeb-3a7e-4a2c-8db2-05ae5f94ccc9" />

(WEEK 05)
<img width="6549" height="1880" alt="Mermaid Chart - Create complex, visual diagrams with text -2025-10-19-145614" src="https://github.com/user-attachments/assets/5ab57361-2c16-44ac-8736-c2850075deaf" />

---

### 1️⃣ Controller Layer

**역할:** 클라이언트 요청을 수신하고 `Service`를 호출한 뒤, `ResponseFactory`를 통해 응답을 반환

**주요 클래스**

* `UserController` — 회원가입, 로그인, 이메일 중복 확인
* `PostController` — 게시글 CRUD
* `CommentController` — 댓글 작성, 삭제, 조회
* `LikeController` — 게시글 좋아요/취소

**리팩토링 포인트**

* `ResponseEntity` 직접 생성 코드 제거 → `ResponseFactory`로 통일
* `Swagger @Operation`, `@ApiResponse` 적용으로 문서화 자동화
* Controller는 “비즈니스 호출”만 담당하도록 SRP 준수

---

### 2️⃣ Service Layer

**역할:** 핵심 비즈니스 로직 수행

**주요 클래스**

* `UserService`
* `PostService`
* `CommentService`
* `LikeService`

**리팩토링 포인트**

* 인증/인가 검증 로직을 별도 Validator로 분리
* 비즈니스 로직 외 Concern 제거 → 단일 책임 강화
* 예외 발생 시 `BusinessException`으로 일원화

---

### 3️⃣ Validation Layer

**역할:** 입력 데이터의 유효성 검증

**주요 클래스**

* `UserValidator`
* `PostValidator`
* `CommentValidator`

**리팩토링 포인트**

* 이메일 형식, 비밀번호 규칙, 제목 길이 등 검증 로직을 Controller로부터 분리
* Validation 실패 시 `BusinessException` throw
* SRP 준수 및 코드 테스트 용이성 향상

---

### 4️⃣ Repository Layer

**역할:** 데이터의 CRUD 관리

**주요 클래스**

* `DummyUserRepository`
* `DummyPostRepository`
* `DummyCommentRepository`
* `DummyLikeRepository`

> 현재는 메모리 기반(Map) 저장소로 구현되었으며,
> 추후 JPA로 교체 시에도 상위 계층 코드 변경이 없도록 **의존성 역전(DIP)** 구조를 유지함.

---

### 5️⃣ Exception Layer

**역할:** 모든 예외를 중앙집중적으로 처리

**주요 클래스**

* `BusinessException`
* `ErrorCode`
* `GlobalExceptionHandler`

**리팩토링 포인트**

* Controller 내부 예외 처리 제거 → Global Handler로 일원화
* Swagger `@ApiResponse`를 통한 예외 문서 자동화
* 일관된 응답 포맷(`ErrorResponse`) 유지

---

### 6️⃣ Response Layer (New)

**역할:** API 응답 구조 표준화

**주요 클래스**

* `ApiResponse`
* `ResponseFactory`

**리팩토링 포인트**

* Controller 전역에서 `ResponseEntity` 생성 중복 제거
* 성공/실패/생성/삭제 등 모든 응답을 `ResponseFactory` 단일 진입점으로 관리
* Swagger 문서에 일관된 응답 구조 자동 반영

```java
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;
}
```

```java
@Component
public class ResponseFactory {
    public <T> ResponseEntity<ApiResponse<T>> ok(T data) { ... }
    public <T> ResponseEntity<ApiResponse<T>> created(T data) { ... }
    public ResponseEntity<ApiResponse<Void>> noContent() { ... }
}
```

