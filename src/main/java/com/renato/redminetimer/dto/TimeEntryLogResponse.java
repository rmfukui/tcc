package com.renato.redminetimer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TimeEntryLogResponse(
        Long id,
        Long redmineEntryId,
        Long issueId,
        BigDecimal hours,
        String comments,
        LocalDateTime syncedAt
) {
}
