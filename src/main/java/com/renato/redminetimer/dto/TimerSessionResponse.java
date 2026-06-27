package com.renato.redminetimer.dto;

import com.renato.redminetimer.entity.TimerStatus;
import java.time.LocalDateTime;

public record TimerSessionResponse(
        Long id,
        Long issueId,
        String issueName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long elapsedSeconds,
        TimerStatus status
) {
}
