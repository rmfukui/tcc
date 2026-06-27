package com.renato.redminetimer.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RedmineIssueResponse(
        Long id,
        String subject,
        RedmineNamedValue project,
        RedmineNamedValue tracker,
        RedmineNamedValue status,
        @JsonProperty("assigned_to")
        RedmineNamedValue assignedTo
) {
}
