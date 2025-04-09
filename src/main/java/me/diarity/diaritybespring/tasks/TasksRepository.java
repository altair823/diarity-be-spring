package me.diarity.diaritybespring.tasks;

import me.diarity.diaritybespring.tasks.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
}
