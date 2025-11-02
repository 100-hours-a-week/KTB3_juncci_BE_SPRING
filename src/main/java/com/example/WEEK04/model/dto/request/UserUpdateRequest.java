package com.example.WEEK04.model.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    @Size(max = 20, message = "닉네임은 20자 이하로 입력해야 합니다.")
    private String nickname;

    private String profileImage;
}
