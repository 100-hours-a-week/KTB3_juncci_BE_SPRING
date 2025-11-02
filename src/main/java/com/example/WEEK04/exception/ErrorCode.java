package com.example.WEEK04.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // ─── User ───────────────────────────────
    EMAIL_DUPLICATE("USR-EMAIL-DUP", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    EMAIL_INVALID("USR-EMAIL-INVALID", "이메일 형식이 올바르지 않습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    PASSWORD_INVALID("USR-PWD-WEAK", "비밀번호는 8~20자이며 대/소문자, 숫자, 특수문자를 포함해야 합니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    NICKNAME_INVALID("USR-NICK-INVALID", "닉네임은 비어 있을 수 없습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    PROFILE_IMAGE_INVALID("USR-IMG-INVALID", "프로필 이미지는 https URL 이어야 합니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    FIELD_MISSING("USR-FIELD-MISSING", "이메일과 비밀번호를 모두 입력해주세요.", HttpStatus.BAD_REQUEST),
    LOGIN_FAIL("USR-LOGIN-FAIL", "아이디 또는 비밀번호를 확인해주세요.", HttpStatus.UNAUTHORIZED),
    USER_WITHDRAWN("USR-WITHDRAWN", "탈퇴한 회원입니다.", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND("USR-NOT-FOUND", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_INACTIVE("USR-INACTIVE", "휴면 상태의 계정입니다.", HttpStatus.FORBIDDEN),
    USER_STATUS_INVALID("USR-STATUS-INVALID", "잘못된 회원 상태입니다.", HttpStatus.BAD_REQUEST),

    // ─── Auth ───────────────────────────────
    AUTH_TOKEN_INVALID("AUTH-TOKEN-INVALID", "유효하지 않거나 만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    AUTH_FORBIDDEN("AUTH-FORBIDDEN", "해당 게시글을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // ─── Post ───────────────────────────────
    POST_FIELD_MISSING("POST-FIELD-MISSING", "제목과 내용을 모두 작성해주세요.", HttpStatus.BAD_REQUEST),
    POST_TITLE_LEN("POST-TITLE-LEN", "최대 제목 길이를 초과했습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    POST_NOT_FOUND("POST-NOT-FOUND", "해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POSTS_PARAM_INVALID("POSTS-PARAM-INVALID", "요청 파라미터가 잘못되었습니다.", HttpStatus.BAD_REQUEST),

    // ─── System ─────────────────────────────
    INTERNAL_SERVER("SYS-UNEXPECTED", "예상치 못한 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ─── Comment ─────────────────────────────
    COMMENT_FIELD_MISSING("COMMENT-FIELD-MISSING", "댓글 내용을 입력해주세요.", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_FOUND("COMMENT-NOT-FOUND", "해당 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    LIKE_ALREADY_EXISTS("LIKE-EXIST", "이미 좋아요를 누른 게시글입니다.", HttpStatus.CONFLICT),
    LIKE_NOT_FOUND("LIKE-NOT-FOUND", "좋아요 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
}
