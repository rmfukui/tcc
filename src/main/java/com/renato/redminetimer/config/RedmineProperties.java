package com.renato.redminetimer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "redmine")
public record RedmineProperties(
        String url,
        String apiKey
) {
}
