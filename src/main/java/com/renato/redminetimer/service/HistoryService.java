package com.renato.redminetimer.service;

import com.renato.redminetimer.dto.TimeEntryLogResponse;
import com.renato.redminetimer.mapper.TimeEntryLogMapper;
import com.renato.redminetimer.repository.TimeEntryLogRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    private final TimeEntryLogRepository timeEntryLogRepository;
    private final TimeEntryLogMapper timeEntryLogMapper;

    public HistoryService(
            TimeEntryLogRepository timeEntryLogRepository,
            TimeEntryLogMapper timeEntryLogMapper
    ) {
        this.timeEntryLogRepository = timeEntryLogRepository;
        this.timeEntryLogMapper = timeEntryLogMapper;
    }

    public List<TimeEntryLogResponse> findAll() {
        return timeEntryLogRepository.findAllByOrderBySyncedAtDesc()
                .stream()
                .map(timeEntryLogMapper::toResponse)
                .toList();
    }
}
