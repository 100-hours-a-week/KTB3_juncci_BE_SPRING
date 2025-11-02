package com.example.WEEK04.model.dto.response;

import com.example.WEEK04.model.entity.Comment;
import java.time.format.DateTimeFormatter;

public class CommentResponse {

    private final Long comment_id;
    private final Long post_id;
    private final String author_name;
    private final String content;
    private final String created_at;

    public CommentResponse(Comment comment) {
        this.comment_id = comment.getId();
        this.post_id = comment.getPost().getId();
        this.author_name = comment.getAuthor().getNickname();
        this.content = comment.getContent();
        this.created_at = comment.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Long getId() { return comment_id; }
    public Long getComment_id() { return comment_id; }
    public Long getPost_id() { return post_id; }
    public String getAuthor_name() { return author_name; }
    public String getContent() { return content; }
    public String getCreated_at() { return created_at; }
}
