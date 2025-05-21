package me.diarity.diaritybespring.posts;

import me.diarity.diaritybespring.posts.dto.PostsWithLikeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    List<Posts> findAllByOrderByCreatedAtDesc();

    @Query(
            "SELECT p, " +
                    "CASE WHEN l.id IS NULL THEN false ELSE true END " +
                    "FROM Posts p " +
                    "LEFT JOIN PostsLikes l ON p.id = l.post.id AND l.user.id = :userId " +
                    "ORDER BY p.createdAt DESC"
    )
    List<Object[]> findAllByOrderByCreatedAtDescWithLiked(@Param("userId") Long userId);

    @Query(
            "SELECT new me.diarity.diaritybespring.posts.dto.PostsWithLikeResponse(" +
                    "p.id, p.bookTitle, p.title, p.content, p.author.email, p.createdAt, p.modifiedAt, " +
                    "p.isPublic, p.isDeleted, p.deletedAt, p.likesCount, p.commentsCount, " +
                    "CASE WHEN l.id IS NULL THEN false ELSE true END) " +
                    "FROM Posts p " +
                    "LEFT JOIN PostsLikes l ON p.id = l.post.id AND l.user.id = :userId " +
                    "WHERE p.id = :postId"
    )
    Optional<PostsWithLikeResponse> findByIdWithLiked(@Param("postId") Long postId, @Param("userId") Long userId);

    Integer countByAuthorId(Long authorId);
}
