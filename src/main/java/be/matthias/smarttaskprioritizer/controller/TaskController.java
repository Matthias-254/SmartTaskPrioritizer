package be.matthias.smarttaskprioritizer.controller;

import be.matthias.smarttaskprioritizer.exception.TaskNotFoundException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tasks/form";
        }

        boolean isNew = (task.getId() == null);

        service.save(task);

        if (isNew) {
            redirectAttributes.addFlashAttribute("successMessage", "Task created successfully.");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Task updated successfully.");
        }

        return "redirect:/tasks";
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {
        service.complete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Task marked as completed.");
        return "redirect:/tasks";
    }

    @GetMapping("/completed")
    public String completed(Model model) {
        model.addAttribute("tasks", repo.findByCompletedTrueOrderByUpdatedAtDesc());
        return "tasks/completed";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        service.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Task deleted.");
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Task task = repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        model.addAttribute("task", task);
        return "tasks/form";
    }
}