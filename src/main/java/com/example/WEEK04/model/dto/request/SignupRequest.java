package com.example.WEEK04.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$",
            message = "비밀번호는 8~20자, 대문자/소문자/숫자/특수문자를 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(max = 20, message = "닉네임은 20자 이하로 입력해야 합니다.")
    private String nickname;

    @Pattern(
            regexp = "^https://.*$",
            message = "프로필 이미지는 https://로 시작해야 합니다."
    )
    private String profile_image;
}
