package be.matthias.smarttaskprioritizer.repository;

import be.matthias.smarttaskprioritizer.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompletedFalseOrderByPriorityScoreDesc();  // Open tasks ordered by highest to lowest priority
    List<Task> findByCompletedTrueOrderByUpdatedAtDesc();  // Completed tasks ordered by most recently updated
    List<Task> findByCompletedFalseOrderByPriorityScoreAsc();  // Open tasks ordered by lowest to highest priority
}