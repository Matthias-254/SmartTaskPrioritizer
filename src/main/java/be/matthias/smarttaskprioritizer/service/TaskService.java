package be.matthias.smarttaskprioritizer.service;

import be.matthias.smarttaskprioritizer.exception.TaskNotFoundException;
import be.matthias.smarttaskprioritizer.model.Task;
import be.matthias.smarttaskprioritizer.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public int calculatePriority(Task task) {
        // Veiligheid: als er geen deadline is, geven we de laagste basis-score
        int deadlineScore = computeDeadlineScore(task.getDeadline());
        int urgencyScore = computeUrgencyScore(task.getUrgency());
        int categoryScore = computeCategoryScore(task.getCategory());
        int ageScore = computeAgeScore(task.getCreatedAt());

        int rawScore = deadlineScore + urgencyScore + categoryScore + ageScore;

        int maxRawScore = 21; // 10 (deadline) + 4 (urgency) + 3 (category) + 4 (age)

        // Converting to a 1-10 scale
        int normalized = (int) Math.round((rawScore / (double) maxRawScore) * 10);

        if (normalized < 1) normalized = 1;
        if (normalized > 10) normalized = 10;

        return normalized;
    }

    private int computeDeadlineScore(LocalDate deadline) {
        if (deadline == null) {
            return 0;
        }

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), deadline);

        if (daysLeft <= 0) return 10;
        if (daysLeft <= 2) return 9;
        if (daysLeft <= 6) return 7;
        if (daysLeft <= 14) return 5;
        if (daysLeft <= 30) return 3;
        return 0;
    }

    private int computeUrgencyScore(String urgency) {
        if (urgency == null) {
            return 0;
        }
        return switch (urgency.toUpperCase()) {
            case "HIGH" -> 4;
            case "MEDIUM" -> 2;
            case "LOW" -> 0;
            default -> 0;
        };
    }

    private int computeCategoryScore(String category) {
        if (category == null) {
            return 0;
        }
        return switch (category.toUpperCase()) {
            case "SCHOOL" -> 3;
            case "WORK" -> 2;
            case "PERSONAL" -> 1;
            case "HOBBY" -> 0;
            default -> 0;
        };
    }

    private int computeAgeScore(LocalDateTime createdAt) {
        if (createdAt == null) {
            return 0;
        }

        long daysOpen = ChronoUnit.DAYS.between(createdAt.toLocalDate(), LocalDate.now());

        if (daysOpen > 30) return 4;
        if (daysOpen > 14) return 3;
        if (daysOpen > 7)  return 2;
        if (daysOpen > 2)  return 1;
        return 0;
    }

    public Task save(Task task) {
        task.setPriorityScore(calculatePriority(task));
        task.setUpdatedAt(LocalDateTime.now());

        if (task.getId() == null) {
            task.setCreatedAt(LocalDateTime.now());
        }

        return repo.save(task);
    }

    public void complete(Long id) {
        Task task = repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setCompleted(true);
        save(task);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}