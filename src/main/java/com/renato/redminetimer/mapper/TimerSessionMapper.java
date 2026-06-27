package com.renato.redminetimer.mapper;

import com.renato.redminetimer.dto.TimerSessionResponse;
import com.renato.redminetimer.entity.TimerSession;
import org.springframework.stereotype.Component;

@Component
public class TimerSessionMapper {

    public TimerSessionResponse toResponse(TimerSession session) {
        return new TimerSessionResponse(
                session.getId(),
                session.getIssueId(),
                session.getIssueName(),
                session.getStartTime(),
                session.getEndTime(),
                session.getElapsedSeconds(),
                session.getStatus()
        );
    }
}
