# KTB3_juncci_BE_SPRING 

과제 프로젝트로,
**회원가입 / 로그인 / 게시판 / 댓글 / 좋아요** 기능을 중심으로 한 RESTful API 서버입니다.
이전 버전은 메모리 기반 `DummyRepository` 구조였으며,
본 버전에서는 **Spring Data JPA 기반 구조로 리팩토링**되었습니다.

(업데이트: 2025.11.02)

---

## 주요 기능

* 사용자 회원가입 / 로그인 / 이메일 중복 확인
* 게시글 작성 · 수정 · 삭제 · 조회
* 댓글 작성 · 삭제 · 조회
* 좋아요 등록 / 취소
* Swagger 기반 API 문서 자동 생성
* 예외 통합 처리 및 응답 포맷 표준화

---

## 프로젝트 구조 (Architecture Overview)

Spring Boot MVC 패턴을 기반으로 **5계층 아키텍처**로 구성되어 있습니다.
각 계층은 단일 책임 원칙(SRP)과 의존성 역전 원칙(DIP)을 준수하도록 설계되었습니다.

| WEEK 04                                                                                                                               | WEEK 05                                                                                                                               |
| ------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| <img width="4710" height="2062" alt="WEEK04" src="https://github.com/user-attachments/assets/e9230aeb-3a7e-4a2c-8db2-05ae5f94ccc9" /> | <img width="6549" height="1880" alt="WEEK05" src="https://github.com/user-attachments/assets/5ab57361-2c16-44ac-8736-c2850075deaf" /> |

WEEK 06 <img width="5936" height="1804" alt="Mermaid Chart - Create complex, visual diagrams with text -2025-10-21-100902" src="https://github.com/user-attachments/assets/d6053fca-39d7-4c84-a413-08f98c153a3a" />

WEEK 07(최신) <img width="4152" height="1522" alt="Mermaid Chart - Create complex, visual diagrams with text -2025-11-02-085719" src="https://github.com/user-attachments/assets/f6abe3a0-c76e-4082-9530-2d8e93a56d3f" />



---


### 1️⃣ Controller Layer

**역할:**
클라이언트의 HTTP 요청을 수신하고, 비즈니스 로직을 수행하는 Service를 호출한 뒤
`ResponseFactory`를 통해 통일된 응답 객체(`ApiResponseDto`)를 반환합니다.

**주요 클래스**

* `UserController` — 회원가입, 로그인, 이메일 중복 확인, 회원정보 수정/삭제
* `PostController` — 게시글 CRUD
* `CommentController` — 댓글 작성/삭제/조회
* `LikeController` — 좋아요 추가/취소/조회

**리팩토링 포인트**

* `ResponseEntity` 생성 코드 제거 → `ResponseFactory`로 통일
* Swagger `@Operation`, `@ApiResponses`로 API 문서 자동화
* Controller는 요청 흐름 제어에만 집중 → SRP 강화

---

### 2️⃣ Service Layer

**역할:**
비즈니스 로직의 핵심 처리 단위로, 데이터 조작과 트랜잭션을 담당합니다.
또한 인증(`AuthService`)을 통해 Authorization 헤더에서 사용자 식별을 수행합니다.

**주요 클래스**

* `UserService` — 회원가입, 로그인, 비밀번호 변경 등 사용자 로직
* `PostService` — 게시글 CRUD, 조회수 및 좋아요 수 반영
* `CommentService` — 댓글 작성/삭제, 게시글별 댓글 조회
* `LikeService` — 좋아요 등록/취소, 개수 조회
* `AuthService` — Authorization 헤더로부터 사용자 식별 ID 추출

**리팩토링 포인트**

* `DummyRepository` → `JpaRepository` 전환
* `@Transactional`로 트랜잭션 경계 명확화
* 비즈니스 예외를 `BusinessException`으로 일원화
* 인증 실패, 권한 부족 등의 로직을 `AuthService`로 분리

---

### 3️⃣ Repository Layer

**역할:**
데이터베이스 접근을 전담하며, CRUD 및 커스텀 쿼리를 수행합니다.
Spring Data JPA의 강점을 활용하여 복잡한 SQL 없이 데이터 조작이 가능합니다.

**주요 클래스**

* `UserRepository`
* `PostRepository`
* `CommentRepository`
* `LikeRepository`

**리팩토링 포인트**

* 기존 Map 기반 더미 저장소 제거
* `@Query`, `@EntityGraph`로 필요한 연관 데이터 즉시 로딩
* `@Transactional` + `@Modifying`으로 성능 최적화 (ex: 조회수 증가 쿼리)
* Repository 변경 시 상위 계층(Service, Controller)에 영향이 없도록 DIP 유지

---

### 4️⃣ Exception Layer

**역할:**
모든 예외를 중앙 집중적으로 처리하여 일관된 에러 응답을 제공합니다.

**주요 클래스**

* `BusinessException` — 사용자 정의 런타임 예외
* `ErrorCode` — 에러 코드, 메시지, HTTP 상태 정의
* `GlobalExceptionHandler` — 전역 예외 처리 및 응답 변환


---

### 5️⃣ Response Layer

**역할:**
모든 API 응답을 표준화된 구조(`code`, `message`, `data`)로 반환합니다.

**주요 클래스**

* `ApiResponseDto` — 공통 응답 DTO
* `ResponseFactory` — 성공/실패/생성/삭제 응답 빌더


---

## API 예시

| Method   | Endpoint                               | Description |
| -------- | -------------------------------------- | ----------- |
| `POST`   | `/users`                               | 회원가입        |
| `POST`   | `/users/auth`                          | 로그인         |
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
 ├── common/             # 공통 응답 및 ResponseFactory
 ├── config/             # Swagger/OpenAPI 설정
 ├── controller/         # REST API 진입점
 ├── exception/          # 예외 정의 및 핸들러
 ├── model/
 │   ├── dto/
 │   │   ├── request/
 │   │   └── response/
 │   ├── entity/
 │   └── enums/
 ├── repository/         # JPA Repository 인터페이스
 └── service/            # 비즈니스 로직
```

---

 
