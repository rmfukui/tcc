package com.renato.redminetimer.integration;

public record RedmineUser(
        Long id,
        String login,
        String firstname,
        String lastname,
        String mail
) {
    public String displayName() {
        String fullName = ((firstname != null ? firstname : "") + " " + (lastname != null ? lastname : "")).trim();
        return fullName.isBlank() ? login : fullName;
    }
}
