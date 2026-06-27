package com.renato.redminetimer.integration;

import java.util.List;

public record RedmineIssuesResponse(
        List<RedmineIssueResponse> issues
) {
}
