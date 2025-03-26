package me.diarity.diaritybespring.posts.comments.repository;

import me.diarity.diaritybespring.posts.comments.dto.CommentsLikesId;
import me.diarity.diaritybespring.posts.comments.entity.CommentsLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsLikesRepository extends JpaRepository<CommentsLikes, CommentsLikesId> {
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
}
