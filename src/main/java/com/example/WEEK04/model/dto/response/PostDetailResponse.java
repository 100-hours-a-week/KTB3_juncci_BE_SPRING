package com.example.WEEK04.model.dto.response;
import com.example.WEEK04.model.entity.Post;

import java.util.List;

public class PostDetailResponse {
    private final String message;
    private final Data data;
    private final Object error;

    public PostDetailResponse(String message, Data data, Object error) {
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public String getMessage() { return message; }
    public Data getData() { return data; }
    public Object getError() { return error; }

    public static class Data {
        private final Long post_id;
        private final String title;
        private final String content;
        private final List<String> images;
        private final String created_at;
        private final int comment_count;
        private final int like_count;
        private final int view_count;

        public Data(Post p) {
            this.post_id = p.getId();
            this.title = p.getTitle();
            this.content = p.getContent();
            this.images = p.getImages();
            this.created_at = p.getCreatedAt();
            this.comment_count = p.getCommentCount();
            this.like_count = p.getLikeCount();
            this.view_count = p.getViewCount();
        }

        public Long getPost_id() { return post_id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public List<String> getImages() { return images; }
        public String getCreated_at() { return created_at; }
        public int getComment_count() { return comment_count; }
        public int getLike_count() { return like_count; }
        public int getView_count() { return view_count; }
    }
}
