package com.renato.redminetimer.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RedmineActivity(
        Long id,
        String name,
        Boolean active,
        @JsonProperty("is_default")
        Boolean defaultActivity
) {
}
