package com.renato.redminetimer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_entry_logs")
public class TimeEntryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long redmineEntryId;

    @Column(nullable = false)
    private Long issueId;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal hours;

    @Column(length = 1000)
    private String comments;

    @Column(nullable = false)
    private LocalDateTime syncedAt;
}
