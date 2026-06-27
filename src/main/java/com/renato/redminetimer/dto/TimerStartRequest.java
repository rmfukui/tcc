package com.renato.redminetimer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TimerStartRequest(
        @NotNull
        Long issueId,

        @NotBlank
        String issueName
) {
}
