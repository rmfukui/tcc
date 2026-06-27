package com.renato.redminetimer.mapper;

import com.renato.redminetimer.dto.TimeEntryLogResponse;
import com.renato.redminetimer.entity.TimeEntryLog;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryLogMapper {

    public TimeEntryLogResponse toResponse(TimeEntryLog log) {
        return new TimeEntryLogResponse(
                log.getId(),
                log.getRedmineEntryId(),
                log.getIssueId(),
                log.getHours(),
                log.getComments(),
                log.getSyncedAt()
        );
    }
}
