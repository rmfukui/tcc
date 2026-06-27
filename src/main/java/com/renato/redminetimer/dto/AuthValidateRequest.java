package com.renato.redminetimer.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthValidateRequest(
        @NotBlank
        String redmineUrl,

        @NotBlank
        String apiKey
) {
}
