package com.example.stresstest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostEntity create(@RequestBody PostEntity post) {
        return postService.writePost(post.getTitle(), post.getContent());
    }

    @GetMapping
    public List<PostEntity> readAll() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostEntity readOne(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 1. 오프셋 페이징 API
    // 예시: GET /posts/offset?page=0&size=10
    @GetMapping("/offset")
    public List<PostEntity> getOffsetPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return postService.getPostsWithOffset(page, size);
    }

    // 2. 커서 페이징 API
    // 예시(첫 요청): GET /posts/cursor?size=10
    // 예시(이후 요청): GET /posts/cursor?lastPostId=154&size=10
    @GetMapping("/cursor")
    public List<PostEntity> getCursorPaging(
            @RequestParam(required = false) Long lastPostId,
            @RequestParam(defaultValue = "10") int size) {
        return postService.getPostsWithCursor(lastPostId, size);
    }
}