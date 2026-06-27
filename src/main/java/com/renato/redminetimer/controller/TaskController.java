package com.renato.redminetimer.controller;

import com.renato.redminetimer.dto.TaskResponse;
import com.renato.redminetimer.service.TaskService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAssignedTasks() {
        return ResponseEntity.ok(taskService.findAssignedTasks());
    }
}
