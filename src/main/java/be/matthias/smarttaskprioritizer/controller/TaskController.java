package be.matthias.smarttaskprioritizer.controller;

import be.matthias.smarttaskprioritizer.repository.TaskRepository;
import be.matthias.smarttaskprioritizer.service.TaskService;
import be.matthias.smarttaskprioritizer.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;
    private final TaskRepository repo;

    public TaskController(TaskService service, TaskRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", repo.findByCompletedFalseOrderByPriorityScoreDesc());
        return "tasks/index";
    }
}