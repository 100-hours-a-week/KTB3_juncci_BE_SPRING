package com.example.WEEK04.model.dto.response;

import com.example.WEEK04.model.entity.Comment;
import java.util.List;

public class CommentListResponse {

    private final String message;
    private final List<CommentResponse> comments;
    private final int total_count;

    public CommentListResponse(List<Comment> commentList) {
        this.message = commentList.isEmpty() ? "no comments" : "ok";
        this.comments = commentList.stream()
                .map(CommentResponse::new)
                .toList();
        this.total_count = commentList.size();
    }

    public String getMessage() { return message; }
    public List<CommentResponse> getComments() { return comments; }
    public int getTotal_count() { return total_count; }
}
