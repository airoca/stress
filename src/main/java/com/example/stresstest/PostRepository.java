package com.example.stresstest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    // 커서 페이징용: 지정한 id보다 작은 게시물을 id 내림차순으로 가져옴 (최신순)
    List<PostEntity> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);

    // 첫 페이지 커서 페이징용: 최신 데이터부터 그냥 가져옴
    List<PostEntity> findAllByOrderByIdDesc(Pageable pageable);
}