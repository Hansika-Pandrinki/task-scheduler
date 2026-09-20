package com.example.taskscheduler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TaskWorker {

    private static final int WORKER_COUNT = 3;
    private static final int MAX_RETRIES = 3;

    private final TaskQueue taskQueue;
    private final TaskRepository repository;
    private ExecutorService executor;

    public TaskWorker(TaskQueue taskQueue, TaskRepository repository) {
        this.taskQueue = taskQueue;
        this.repository = repository;
    }

    @PostConstruct
    public void start() {
        executor = Executors.newFixedThreadPool(WORKER_COUNT);
        for (int i = 0; i < WORKER_COUNT; i++) {
            executor.submit(this::runWorker);
        }
    }

    private void runWorker() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Task task = taskQueue.take();
                process(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void process(Task task) throws InterruptedException {
        task.setStatus(TaskStatus.RUNNING);
        repository.save(task);

        try {
            Thread.sleep(3000); // pretend to do work for 3 seconds

            // a task with "fail" in its name always fails (for testing retry)
            if (task.getName().toLowerCase().contains("fail")) {
                throw new RuntimeException("Simulated failure");
            }

            task.setStatus(TaskStatus.COMPLETED);
            repository.save(task);

        } catch (RuntimeException e) {
            task.setRetryCount(task.getRetryCount() + 1);
            if (task.getRetryCount() < MAX_RETRIES) {
                task.setStatus(TaskStatus.PENDING);
                repository.save(task);
                taskQueue.add(task); // put back in the line and try again
            } else {
                task.setStatus(TaskStatus.FAILED);
                repository.save(task);
            }
        }
    }

    @PreDestroy
    public void stop() {
        executor.shutdownNow();
    }
}