package com.example.WEEK04.model.dto.response;

import com.example.WEEK04.model.entity.User;
import com.example.WEEK04.model.enums.UserStatus;

public class UserResponse {
    private Long id;
    private String email;
    private String nickname;
    private String profileImage;
    private UserStatus status;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.status = user.getStatus();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public UserStatus getStatus() {
        return status;
    }
}
