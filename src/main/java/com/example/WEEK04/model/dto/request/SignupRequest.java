package com.example.WEEK04.model.dto.request;

public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
    private String profile_image;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public String getProfile_image() { return profile_image; }
}