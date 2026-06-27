package com.renato.redminetimer.repository;

import com.renato.redminetimer.entity.TimerSession;
import com.renato.redminetimer.entity.TimerStatus;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimerSessionRepository extends JpaRepository<TimerSession, Long> {

    Optional<TimerSession> findFirstByStatusOrderByStartTimeDesc(TimerStatus status);

    Optional<TimerSession> findFirstByStatusInOrderByStartTimeDesc(Collection<TimerStatus> statuses);
}
