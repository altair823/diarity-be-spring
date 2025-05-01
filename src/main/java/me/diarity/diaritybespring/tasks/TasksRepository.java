package me.diarity.diaritybespring.tasks;

import me.diarity.diaritybespring.tasks.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findAllByUserIdOrderByCreatedAtDesc(Long id);
}
