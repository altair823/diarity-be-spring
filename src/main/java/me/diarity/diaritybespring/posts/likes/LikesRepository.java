package me.diarity.diaritybespring.posts.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, LikesId> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
}
