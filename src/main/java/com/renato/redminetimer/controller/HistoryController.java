package com.renato.redminetimer.controller;

import com.renato.redminetimer.dto.TimeEntryLogResponse;
import com.renato.redminetimer.service.HistoryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public ResponseEntity<List<TimeEntryLogResponse>> findAll() {
        return ResponseEntity.ok(historyService.findAll());
    }
}
