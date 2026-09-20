package com.example.taskscheduler;

import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class TaskQueue {

    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>(
            11,
            Comparator.comparingInt(Task::getPriority).reversed()
                      .thenComparing(Task::getId)
    );

    public void add(Task task) {
        queue.add(task);
    }

    public Task take() throws InterruptedException {
        return queue.take(); // waits here if the queue is empty
    }
}
