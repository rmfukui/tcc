package com.renato.redminetimer.mapper;

import com.renato.redminetimer.dto.TaskResponse;
import com.renato.redminetimer.integration.RedmineIssueResponse;
import com.renato.redminetimer.integration.RedmineNamedValue;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(RedmineIssueResponse issue) {
        return new TaskResponse(
                issue.id(),
                issue.subject(),
                getName(issue.project()),
                getName(issue.status()),
                getName(issue.tracker())
        );
    }

    private String getName(RedmineNamedValue value) {
        return value != null ? value.name() : null;
    }
}
