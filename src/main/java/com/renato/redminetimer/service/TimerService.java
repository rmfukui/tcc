package com.renato.redminetimer.service;

import com.renato.redminetimer.dto.TimerFinishRequest;
import com.renato.redminetimer.dto.TimerSessionResponse;
import com.renato.redminetimer.dto.TimerStartRequest;
import com.renato.redminetimer.entity.TimeEntryLog;
import com.renato.redminetimer.entity.TimerSession;
import com.renato.redminetimer.entity.TimerStatus;
import com.renato.redminetimer.exception.BusinessException;
import com.renato.redminetimer.integration.RedmineClient;
import com.renato.redminetimer.mapper.TimerSessionMapper;
import com.renato.redminetimer.repository.TimeEntryLogRepository;
import com.renato.redminetimer.repository.TimerSessionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class TimerService {

    private final TimerSessionRepository timerSessionRepository;
    private final TimeEntryLogRepository timeEntryLogRepository;
    private final RedmineClient redmineClient;
    private final TimerSessionMapper timerSessionMapper;

    public TimerService(
            TimerSessionRepository timerSessionRepository,
            TimeEntryLogRepository timeEntryLogRepository,
            RedmineClient redmineClient,
            TimerSessionMapper timerSessionMapper
    ) {
        this.timerSessionRepository = timerSessionRepository;
        this.timeEntryLogRepository = timeEntryLogRepository;
        this.redmineClient = redmineClient;
        this.timerSessionMapper = timerSessionMapper;
    }

    @Transactional
    public TimerSessionResponse start(TimerStartRequest request) {
        timerSessionRepository.findFirstByStatusInOrderByStartTimeDesc(List.of(TimerStatus.RUNNING, TimerStatus.PAUSED))
                .ifPresent(session -> {
                    throw new BusinessException("Ja existe um timer em andamento.");
                });

        TimerSession session = TimerSession.builder()
                .issueId(request.issueId())
                .issueName(request.issueName())
                .startTime(LocalDateTime.now())
                .elapsedSeconds(0L)
                .status(TimerStatus.RUNNING)
                .build();

        return timerSessionMapper.toResponse(timerSessionRepository.save(session));
    }

    @Transactional
    public TimerSessionResponse pause() {
        TimerSession session = findRunningSession();
        LocalDateTime now = LocalDateTime.now();

        session.setElapsedSeconds(session.getElapsedSeconds() + secondsBetween(session.getStartTime(), now));
        session.setEndTime(now);
        session.setStatus(TimerStatus.PAUSED);

        return timerSessionMapper.toResponse(timerSessionRepository.save(session));
    }

    @Transactional
    public TimerSessionResponse resume() {
        TimerSession session = findPausedSession();

        session.setStartTime(LocalDateTime.now());
        session.setEndTime(null);
        session.setStatus(TimerStatus.RUNNING);

        return timerSessionMapper.toResponse(timerSessionRepository.save(session));
    }

    @Transactional
    public TimerSessionResponse finish(TimerFinishRequest request) {
        TimerSession session = findActiveSession();
        LocalDateTime now = LocalDateTime.now();
        Long totalSeconds = session.getElapsedSeconds();

        if (session.getStatus() == TimerStatus.RUNNING) {
            totalSeconds += secondsBetween(session.getStartTime(), now);
        }

        BigDecimal hours = secondsToHours(totalSeconds);
        String comments = getComments(request, session);
        Long redmineEntryId = redmineClient.createTimeEntry(session.getIssueId(), hours, comments);

        session.setElapsedSeconds(totalSeconds);
        session.setEndTime(now);
        session.setStatus(TimerStatus.FINISHED);

        timeEntryLogRepository.save(TimeEntryLog.builder()
                .redmineEntryId(redmineEntryId)
                .issueId(session.getIssueId())
                .hours(hours)
                .comments(comments)
                .syncedAt(now)
                .build());

        return timerSessionMapper.toResponse(timerSessionRepository.save(session));
    }

    private TimerSession findRunningSession() {
        return timerSessionRepository.findFirstByStatusOrderByStartTimeDesc(TimerStatus.RUNNING)
                .orElseThrow(() -> new BusinessException("Nao existe timer em execucao."));
    }

    private TimerSession findPausedSession() {
        return timerSessionRepository.findFirstByStatusOrderByStartTimeDesc(TimerStatus.PAUSED)
                .orElseThrow(() -> new BusinessException("Nao existe timer pausado."));
    }

    private TimerSession findActiveSession() {
        return timerSessionRepository.findFirstByStatusInOrderByStartTimeDesc(List.of(TimerStatus.RUNNING, TimerStatus.PAUSED))
                .orElseThrow(() -> new BusinessException("Nao existe timer ativo para finalizar."));
    }

    private Long secondsBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).getSeconds();
    }

    private BigDecimal secondsToHours(Long seconds) {
        BigDecimal hours = BigDecimal.valueOf(seconds)
                .divide(BigDecimal.valueOf(3600), 2, RoundingMode.HALF_UP);

        if (hours.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            return BigDecimal.valueOf(0.01);
        }

        return hours;
    }

    private String getComments(TimerFinishRequest request, TimerSession session) {
        if (request != null && StringUtils.hasText(request.comments())) {
            return request.comments();
        }

        return "Apontamento automatico - " + session.getIssueName();
    }
}
