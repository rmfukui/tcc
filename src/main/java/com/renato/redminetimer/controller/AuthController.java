package com.renato.redminetimer.controller;

import com.renato.redminetimer.dto.AuthValidateRequest;
import com.renato.redminetimer.dto.AuthValidateResponse;
import com.renato.redminetimer.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/validate")
    public ResponseEntity<AuthValidateResponse> validate(@Valid @RequestBody AuthValidateRequest request) {
        return ResponseEntity.ok(authService.validate(request));
    }
}
