package me.diarity.diaritybespring.posts.comments.repository;

import me.diarity.diaritybespring.posts.comments.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByPostId(Long postId);
}
