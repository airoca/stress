package com.example.stresstest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PostBulkInsertTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void insertBulkData() {
        int totalRows = 3_000_000; // 총 넣을 데이터 수
        int batchSize = 10_000;    // 한 번에 보낼 배치 크기

        String sql = "INSERT INTO post_entity (title, content) VALUES (?, ?)";

        List<PostDto> batchArgs = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        System.out.println("=== 대량 데이터 삽입 시작 ===");

        for (int i = 1; i <= totalRows; i++) {
            batchArgs.add(new PostDto("제목 테스트 데이터입니다. 번호: " + i, "본문 내용 테스트입니다. 대량 부하 테스트를 위한 더미 데이터 공간입니다. " + i));

            // 설정한 배치 크기에 도달하거나 마지막 데이터일 때 DB로 전송
            if (i % batchSize == 0 || i == totalRows) {
                final List<PostDto> currentBatch = new ArrayList<>(batchArgs);

                jdbcTemplate.batchUpdate(sql,
                        new org.springframework.jdbc.core.BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
                                PostDto post = currentBatch.get(i);
                                ps.setString(1, post.title);
                                ps.setString(2, post.content);
                            }

                            @Override
                            public int getBatchSize() {
                                return currentBatch.size();
                            }
                        }
                );

                batchArgs.clear(); // 메모리 비우기
                System.out.printf("[%d / %d] 건 삽입 완료...\n", i, totalRows);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("=== 대량 데이터 삽입 완료! 소요시간: %d ms ===\n", (endTime - startTime));
    }

    // 테스트용 간단한 inner DTO
    static class PostDto {
        String title;
        String content;
        PostDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
