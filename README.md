# Task Scheduler

A web app that runs tasks in the background using a priority queue and worker threads. Users add tasks from a web page, workers process them by priority, and the page shows the live status of every task.

**Live demo:** _(link will be added after deployment)_

## Problem it solves

Long-running work makes users wait and slows the app down. This project puts tasks in a queue and processes them in the background, so users don't wait. Urgent tasks run first, and failed tasks are retried automatically.

## Features

- Add tasks with High, Medium, or Low priority
- Priority queue: higher priority runs first, and older tasks go first when priority is equal
- 3 worker threads process tasks at the same time
- Status tracking: PENDING → RUNNING → COMPLETED or FAILED
- Automatic retry (up to 3 times) for failed tasks
- Live dashboard that refreshes every 2 seconds

## Tech Stack

Java 17, Spring Boot, Spring Data JPA, H2 database, HTML/CSS/JavaScript, Maven, Docker

## API Endpoints

| Method | Endpoint | What it does |
|--------|----------|--------------|
| POST | /tasks | Add a new task |
| GET | /tasks | List all tasks |
| GET | /tasks/{id} | Get one task |

Example request body for `POST /tasks`:

```json
{ "name": "Send email", "priority": 3 }
```

## How to Run

You need JDK 17 installed.

```
git clone https://github.com/Hansika-Pandrinki/task-scheduler.git
cd task-scheduler
.\mvnw spring-boot:run
```

On Mac or Linux, use `./mvnw spring-boot:run`. Then open http://localhost:8080

## Screenshots

![Dashboard](docs/screenshot1.png)

![Tasks running](docs/screenshot2.png)

## How It Works

1. The user submits a task from the web page.
2. The task is saved in the database with status PENDING and added to a priority queue.
3. A free worker thread takes the highest-priority task and changes its status to RUNNING.
4. When the work finishes, the status becomes COMPLETED. If it fails, it is retried up to 3 times, then marked FAILED.
5. The web page asks the server for updates every 2 seconds and shows the new status.

## Future Improvements

- Switch to MySQL or PostgreSQL so tasks are saved permanently
- Scheduled tasks (run at a set time)
- User login
- Use RabbitMQ or Kafka for the queue