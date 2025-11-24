package be.matthias.smarttaskprioritizer.controller;

import be.matthias.smarttaskprioritizer.exception.TaskNotFoundException;
import be.matthias.smarttaskprioritizer.model.Task;
import be.matthias.smarttaskprioritizer.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskApiController {

    private final TaskRepository repo;

    public TaskApiController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Task> getOpenTasks() {
        return repo.findByCompletedFalseOrderByPriorityScoreDesc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = repo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return ResponseEntity.ok(task);
    }
}