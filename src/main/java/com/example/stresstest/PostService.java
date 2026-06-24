package com.example.stresstest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostEntity writePost(String title, String content) {
        PostEntity post = new PostEntity(title, content);
        return postRepository.save(post);
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    public PostEntity getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
    }

    // 1. 오프셋 페이징 조회
    public List<PostEntity> getPostsWithOffset(int page, int size) {
        // ID 내림차순(최신순)으로 페이징 요청 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return postRepository.findAll(pageable).getContent();
    }

    // 2. 커서 페이징 조회
    public List<PostEntity> getPostsWithCursor(Long lastPostId, int size) {
        Pageable pageable = PageRequest.of(0, size); // 내부 limit 처리를 위함

        if (lastPostId == null || lastPostId == 0) {
            // 첫 페이지 요청 시 최신 데이터부터 조회
            return postRepository.findAllByOrderByIdDesc(pageable);
        }

        // 두 번째 페이지부터는 마지막 본 ID보다 작은 데이터 조회
        return postRepository.findByIdLessThanOrderByIdDesc(lastPostId, pageable);
    }
}