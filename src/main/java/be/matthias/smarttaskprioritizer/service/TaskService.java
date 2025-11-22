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
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());

        if (daysLeft <= 0) return 10;      // deadline vandaag of voorbij → super urgent
        if (daysLeft <= 2) return 8;       // binnen 2 dagen
        if (daysLeft <= 5) return 5;       // binnen 5 dagen
        return 2;                          // ver in de toekomst
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