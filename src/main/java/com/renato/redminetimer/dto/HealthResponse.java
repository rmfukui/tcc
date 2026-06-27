package com.renato.redminetimer.dto;

import java.time.LocalDateTime;

public record HealthResponse(
        String status,
        String application,
        LocalDateTime timestamp
) {
}
