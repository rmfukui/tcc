package com.renato.redminetimer.repository;

import com.renato.redminetimer.entity.TimeEntryLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeEntryLogRepository extends JpaRepository<TimeEntryLog, Long> {

    List<TimeEntryLog> findAllByOrderBySyncedAtDesc();
}
