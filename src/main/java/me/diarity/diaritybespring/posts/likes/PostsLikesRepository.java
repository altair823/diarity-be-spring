package me.diarity.diaritybespring.posts.likes;

import me.diarity.diaritybespring.posts.likes.dto.PostsLikesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsLikesRepository extends JpaRepository<PostsLikes, PostsLikesId> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostIdAndUserId(Long postId, Long userId);
}
