package com.example.WEEK04.model.dto.response;

import com.example.WEEK04.model.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public record PostListResponse(String message, Data data, Object error) {

    public static class Data {
        private final List<PostSummary> posts;
        private final int page;
        private final int size;
        private final int total;


        public Data(List<PostSummary> postSummaries, int page, int size, int total) {
            this.posts = postSummaries;
            this.page = page;
            this.size = size;
            this.total = total;
        }

        public List<PostSummary> getPosts() { return posts; }
        public int getPage() { return page; }
        public int getSize() { return size; }
        public int getTotal() { return total; }
    }

    @Getter
    public static class PostSummary {
        private final Long post_id;
        private final String title;
        private final LocalDateTime created_at;
        private final int comment_count;
        private final int like_count;
        private final int view_count;
        private final Long author_id;
        private final String author_nickname;

        public PostSummary(
                Long post_id,
                String title,
                LocalDateTime created_at,
                int comment_count,
                int like_count,
                int view_count,
                Long author_id,
                String author_nickname
        ) {
            this.post_id = post_id;
            this.title = title;
            this.created_at = created_at;
            this.comment_count = comment_count;
            this.like_count = like_count;
            this.view_count = view_count;
            this.author_id = author_id;
            this.author_nickname = author_nickname;
        }
    }



}
