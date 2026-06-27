package com.renato.redminetimer.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RedmineTimeEntryResponse(
        @JsonProperty("time_entry")
        RedmineTimeEntry timeEntry
) {
}
