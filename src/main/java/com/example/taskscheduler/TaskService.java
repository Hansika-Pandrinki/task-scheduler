package com.example.taskscheduler;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskQueue taskQueue;

    public TaskService(TaskRepository repository, TaskQueue taskQueue) {
        this.repository = repository;
        this.taskQueue = taskQueue;
    }

    public Task submit(String name, int priority) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task name is required");
        }
        if (priority < 1 || priority > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Priority must be between 1 and 10");
        }
        Task saved = repository.save(new Task(name.trim(), priority));
        taskQueue.add(saved);
        return saved;
    }

    public List<Task> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Task getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
}