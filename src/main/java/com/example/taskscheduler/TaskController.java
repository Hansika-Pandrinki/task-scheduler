package com.example.taskscheduler;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public Task addTask(@RequestBody TaskRequest request) {
        return service.submit(request.name(), request.priority());
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return service.getById(id);
    }
}