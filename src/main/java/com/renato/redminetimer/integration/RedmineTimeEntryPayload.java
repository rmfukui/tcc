package com.renato.redminetimer.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record RedmineTimeEntryPayload(
        @JsonProperty("issue_id")
        Long issueId,

        BigDecimal hours,

        @JsonProperty("activity_id")
        Long activityId,

        @JsonProperty("spent_on")
        String spentOn,

        String comments
) {
}
