package me.diarity.diaritybespring.posts.comments.repository;

import me.diarity.diaritybespring.posts.comments.dto.CommentsHierarchyId;
import me.diarity.diaritybespring.posts.comments.entity.CommentsHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentsHierarchyRepository extends JpaRepository<CommentsHierarchy, CommentsHierarchyId> {
    Optional<CommentsHierarchy> findByChildCommentsId(Long childCommentsId);
}
