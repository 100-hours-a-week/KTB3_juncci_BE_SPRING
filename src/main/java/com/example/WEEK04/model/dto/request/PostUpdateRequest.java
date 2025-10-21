package com.example.WEEK04.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class PostUpdateRequest {

    @NotBlank(message = "제목은 비어 있을 수 없습니다.")
    @Size(max = 26, message = "제목은 최대 26자까지 가능합니다.")
    private String title;

    @NotBlank(message = "내용은 비어 있을 수 없습니다.")
    private String content;

    private List<String> images;

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public List<String> getImages() { return images; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setImages(List<String> images) { this.images = images; }
}
