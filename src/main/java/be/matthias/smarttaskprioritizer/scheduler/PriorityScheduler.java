package be.matthias.smarttaskprioritizer.scheduler;

import be.matthias.smarttaskprioritizer.model.Task;
import be.matthias.smarttaskprioritizer.repository.TaskRepository;
import be.matthias.smarttaskprioritizer.service.TaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriorityScheduler {

    private final TaskRepository repo;
    private final TaskService service;

    public PriorityScheduler(TaskRepository repo, TaskService service) {
        this.repo = repo;
        this.service = service;
    }

    // elke minuut (60000 ms) alle priorities herberekenen
    @Scheduled(fixedRate = 60000)
    public void recalculatePriorities() {
        for (Task task : repo.findAll()) {
            task.setPriorityScore(service.calculatePriority(task));
            repo.save(task);
        }
        System.out.println("[Scheduler] Recalculated task priorities");
    }
}