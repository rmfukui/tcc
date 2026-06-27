package com.renato.redminetimer.controller;

import com.renato.redminetimer.dto.TimerFinishRequest;
import com.renato.redminetimer.dto.TimerSessionResponse;
import com.renato.redminetimer.dto.TimerStartRequest;
import com.renato.redminetimer.service.TimerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timer")
public class TimerController {

    private final TimerService timerService;

    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @PostMapping("/start")
    public ResponseEntity<TimerSessionResponse> start(@Valid @RequestBody TimerStartRequest request) {
        return ResponseEntity.ok(timerService.start(request));
    }

    @PostMapping("/pause")
    public ResponseEntity<TimerSessionResponse> pause() {
        return ResponseEntity.ok(timerService.pause());
    }

    @PostMapping("/resume")
    public ResponseEntity<TimerSessionResponse> resume() {
        return ResponseEntity.ok(timerService.resume());
    }

    @PostMapping("/finish")
    public ResponseEntity<TimerSessionResponse> finish(@RequestBody(required = false) TimerFinishRequest request) {
        return ResponseEntity.ok(timerService.finish(request));
    }
}
