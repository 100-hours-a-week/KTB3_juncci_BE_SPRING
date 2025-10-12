package com.example.WEEK04.model.dto.request;

import java.util.List;

public class PostCreateRequest {
    private String title;
    private String content;
    private List<String> images;

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public List<String> getImages() { return images; }
}
