package com.renato.redminetimer.service;

import com.renato.redminetimer.dto.AuthValidateRequest;
import com.renato.redminetimer.dto.AuthValidateResponse;
import com.renato.redminetimer.integration.RedmineClient;
import com.renato.redminetimer.integration.RedmineUser;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final RedmineClient redmineClient;
    private final UserConfigService userConfigService;

    public AuthService(RedmineClient redmineClient, UserConfigService userConfigService) {
        this.redmineClient = redmineClient;
        this.userConfigService = userConfigService;
    }

    public AuthValidateResponse validate(AuthValidateRequest request) {
        RedmineUser user = redmineClient.validateCredentials(request.redmineUrl(), request.apiKey());
        userConfigService.save(request.redmineUrl(), request.apiKey());

        return new AuthValidateResponse(true, request.redmineUrl(), user.displayName());
    }
}
