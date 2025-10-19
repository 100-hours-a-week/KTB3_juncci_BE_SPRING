package com.example.WEEK04.model.dto.response;

import com.example.WEEK04.model.entity.Comment;
import java.util.List;
import java.util.stream.Collectors;

public class CommentListResponse {

    private final String message;
    private final List<CommentResponse> comments;

    public CommentListResponse(List<Comment> commentList) {
        this.message = "ok";
        this.comments = commentList.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    public String getMessage() { return message; }
    public List<CommentResponse> getComments() { return comments; }
}
