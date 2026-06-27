package com.renato.redminetimer.service;

import com.renato.redminetimer.config.RedmineProperties;
import com.renato.redminetimer.entity.UserConfig;
import com.renato.redminetimer.exception.BusinessException;
import com.renato.redminetimer.repository.UserConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserConfigService {

    private final UserConfigRepository userConfigRepository;
    private final RedmineProperties redmineProperties;

    public UserConfigService(
            UserConfigRepository userConfigRepository,
            RedmineProperties redmineProperties
    ) {
        this.userConfigRepository = userConfigRepository;
        this.redmineProperties = redmineProperties;
    }

    public UserConfig getActiveConfig() {
        return userConfigRepository.findFirstByOrderByIdDesc()
                .orElseGet(this::getEnvironmentConfig);
    }

    public UserConfig save(String redmineUrl, String apiKey) {
        UserConfig config = userConfigRepository.findFirstByOrderByIdDesc()
                .orElseGet(UserConfig::new);

        config.setRedmineUrl(redmineUrl);
        config.setApiKey(apiKey);

        return userConfigRepository.save(config);
    }

    private UserConfig getEnvironmentConfig() {
        if (!StringUtils.hasText(redmineProperties.url()) || !StringUtils.hasText(redmineProperties.apiKey())) {
            throw new BusinessException("Configure a URL e API key do Redmine antes de continuar.");
        }

        return UserConfig.builder()
                .redmineUrl(redmineProperties.url())
                .apiKey(redmineProperties.apiKey())
                .build();
    }
}
