package be.matthias.smarttaskprioritizer.controller;

import be.matthias.smarttaskprioritizer.repository.TaskRepository;
import be.matthias.smarttaskprioritizer.service.TaskService;
import be.matthias.smarttaskprioritizer.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("task") Task task,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "tasks/form";
        }
        service.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable Long id) {
        service.complete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/completed")
    public String completed(Model model) {
        model.addAttribute("tasks", repo.findByCompletedTrueOrderByUpdatedAtDesc());
        return "tasks/completed";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Task task = repo.findById(id).orElseThrow();
        model.addAttribute("task", task);
        return "tasks/form";
    }
}