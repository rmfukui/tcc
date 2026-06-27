package com.renato.redminetimer.service;

import com.renato.redminetimer.dto.TaskResponse;
import com.renato.redminetimer.integration.RedmineClient;
import com.renato.redminetimer.mapper.TaskMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final RedmineClient redmineClient;
    private final TaskMapper taskMapper;

    public TaskService(RedmineClient redmineClient, TaskMapper taskMapper) {
        this.redmineClient = redmineClient;
        this.taskMapper = taskMapper;
    }

    public List<TaskResponse> findAssignedTasks() {
        return redmineClient.findAssignedIssues()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }
}
