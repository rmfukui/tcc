package com.renato.redminetimer.dto;

public record TaskResponse(
        Long id,
        String subject,
        String projectName,
        String statusName,
        String trackerName
) {
}
