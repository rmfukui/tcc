package com.renato.redminetimer.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RedmineActivitiesResponse(
        @JsonProperty("time_entry_activities")
        List<RedmineActivity> timeEntryActivities
) {
}
