package com.example.WEEK04.model.dto.response;

import com.example.WEEK04.model.entity.Post;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record PostDetailResponse(String message, Data data, Object error) {

    public static class Data {

        private final Long post_id;
        private final String title;
        private final String content;
        private final List<String> images;
        private final LocalDateTime created_at;
        private final int comment_count;
        private final int like_count;
        private final int view_count;

        public Data(Post p, int likeCount, int viewCount) {
            this.post_id = p.getId();
            this.title = p.getTitle();
            this.content = p.getContent();


            this.images = (p.getImages() != null && !p.getImages().isBlank())
                    ? Arrays.asList(p.getImages().split(","))
                    : List.of();

            this.created_at = p.getCreatedAt();
            this.comment_count = p.getCommentCount();
            this.like_count = likeCount;
            this.view_count = viewCount;
        }

        public Long getPost_id() {
            return post_id;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public List<String> getImages() {
            return images;
        }

        public LocalDateTime getCreated_at() {
            return created_at;
        }

        public int getComment_count() {
            return comment_count;
        }

        public int getLike_count() {
            return like_count;
        }

        public int getView_count() {
            return view_count;
        }
    }
}
