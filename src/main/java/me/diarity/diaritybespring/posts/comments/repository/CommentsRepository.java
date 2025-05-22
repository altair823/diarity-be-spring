package me.diarity.diaritybespring.posts.comments.repository;

import me.diarity.diaritybespring.posts.comments.dto.CommentsWithLikeResponse;
import me.diarity.diaritybespring.posts.comments.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByPostId(Long postId);

    @Query(
            "SELECT new me.diarity.diaritybespring.posts.comments.dto.CommentsWithLikeResponse(" +
                    "c.id, u.id, u.displayName, u.picture, " +
                    "c.createdAt, c.modifiedAt, c.content, " +
                    "c.isDeleted, c.deletedAt, c.likesCount, " +
                    "c.post.id, ch.parentComments.id, " +
                    "CASE WHEN cl.id IS NULL THEN false ELSE true END) " +
                    "FROM Comments c " +
                    "LEFT JOIN CommentsLikes cl ON c.id = cl.comment.id AND cl.user.id = :userId " +
                    "LEFT JOIN Users u ON c.author.id = u.id " +
                    "LEFT JOIN CommentsHierarchy ch ON c.id = ch.childComments.id " +
                    "WHERE c.post.id = :postId"
    )
    List<CommentsWithLikeResponse> findAllByPostIdWithLike(@Param("postId") Long postId, @Param("userId") Long userId);

    Integer countByAuthorId(Long authorId);
}
