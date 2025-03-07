package me.diarity.diaritybespring.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface PostsRepository extends JpaRepository<Posts, Long> {
    List<Posts> findAllByOrderByCreatedAtDesc();

    @Query(
            "SELECT p, " +
                    "CASE WHEN l.id IS NULL THEN false ELSE true END " +
                    "FROM Posts p " +
                    "LEFT JOIN Likes l ON p.id = l.post.id AND l.user.id = :userId " +
                    "ORDER BY p.createdAt DESC"
    )
    List<Object[]> findAllByOrderByCreatedAtDescWithLiked(@Param("userId") Long userId);
}
