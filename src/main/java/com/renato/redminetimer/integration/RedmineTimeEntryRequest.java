package com.renato.redminetimer.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RedmineTimeEntryRequest(
        @JsonProperty("time_entry")
        RedmineTimeEntryPayload timeEntry
) {
    public static RedmineTimeEntryRequest of(
            Long issueId,
            BigDecimal hours,
            Long activityId,
            LocalDate spentOn,
            String comments
    ) {
        return new RedmineTimeEntryRequest(new RedmineTimeEntryPayload(
                issueId,
                hours,
                activityId,
                spentOn.toString(),
                comments
        ));
    }
}
