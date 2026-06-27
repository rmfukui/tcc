package com.renato.redminetimer.dto;

public record AuthValidateResponse(
        boolean valid,
        String redmineUrl,
        String username
) {
}
