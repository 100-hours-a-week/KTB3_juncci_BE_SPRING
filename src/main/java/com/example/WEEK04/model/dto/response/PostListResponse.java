package com.example.WEEK04.model.dto.response;

import com.example.WEEK04.model.entity.Post;

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

    public static class PostSummary {
        private final Long post_id;
        private final String title;
        private final LocalDateTime created_at;
        private final int comment_count;
        private final int like_count;
        private final int view_count;

        public PostSummary(Post p, int likeCount, int viewCount) {
            this.post_id = p.getId();
            this.title = p.getTitle();
            this.created_at = p.getCreatedAt();
            this.comment_count = p.getCommentCount(); // 사용 안 하면 0 또는 제거 가능
            this.like_count = likeCount;
            this.view_count = viewCount;
        }

        public Long getPost_id() { return post_id; }
        public String getTitle() { return title; }
        public LocalDateTime getCreated_at() { return created_at; }
        public int getComment_count() { return comment_count; }
        public int getLike_count() { return like_count; }
        public int getView_count() { return view_count; }
    }
}
