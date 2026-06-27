package com.renato.redminetimer.integration;

import com.renato.redminetimer.entity.UserConfig;
import com.renato.redminetimer.exception.RedmineIntegrationException;
import com.renato.redminetimer.service.UserConfigService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class RedmineClient {

    private final WebClient.Builder webClientBuilder;
    private final UserConfigService userConfigService;

    public RedmineClient(WebClient.Builder webClientBuilder, UserConfigService userConfigService) {
        this.webClientBuilder = webClientBuilder;
        this.userConfigService = userConfigService;
    }

    public List<RedmineIssueResponse> findAssignedIssues() {
        UserConfig config = userConfigService.getActiveConfig();

        try {
            RedmineIssuesResponse response = buildClient(config)
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/issues.json")
                            .queryParam("assigned_to_id", "me")
                            .build())
                    .retrieve()
                    .bodyToMono(RedmineIssuesResponse.class)
                    .block();

            if (response == null || response.issues() == null) {
                return List.of();
            }

            return response.issues();
        } catch (WebClientResponseException exception) {
            throw new RedmineIntegrationException(
                    "Falha ao buscar tarefas no Redmine. Status: " + exception.getStatusCode().value(),
                    exception
            );
        } catch (Exception exception) {
            throw new RedmineIntegrationException("Nao foi possivel conectar ao Redmine.", exception);
        }
    }

    public RedmineUser validateCredentials(String redmineUrl, String apiKey) {
        UserConfig config = UserConfig.builder()
                .redmineUrl(redmineUrl)
                .apiKey(apiKey)
                .build();

        try {
            RedmineUserResponse response = buildClient(config)
                    .get()
                    .uri("/users/current.json")
                    .retrieve()
                    .bodyToMono(RedmineUserResponse.class)
                    .block();

            if (response == null || response.user() == null) {
                throw new RedmineIntegrationException("Credenciais do Redmine invalidas.");
            }

            return response.user();
        } catch (WebClientResponseException exception) {
            throw new RedmineIntegrationException(
                    "Falha ao validar credenciais do Redmine. Status: " + exception.getStatusCode().value(),
                    exception
            );
        } catch (RedmineIntegrationException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new RedmineIntegrationException("Nao foi possivel validar as credenciais do Redmine.", exception);
        }
    }

    public Long createTimeEntry(Long issueId, BigDecimal hours, String comments) {
        UserConfig config = userConfigService.getActiveConfig();
        Long activityId = findTimeEntryActivityId(config);

        try {
            RedmineTimeEntryResponse response = buildClient(config)
                    .post()
                    .uri("/time_entries.json")
                    .bodyValue(RedmineTimeEntryRequest.of(issueId, hours, activityId, LocalDate.now(), comments))
                    .retrieve()
                    .bodyToMono(RedmineTimeEntryResponse.class)
                    .block();

            if (response == null || response.timeEntry() == null) {
                return null;
            }

            return response.timeEntry().id();
        } catch (WebClientResponseException exception) {
            throw new RedmineIntegrationException(
                    "Falha ao registrar horas no Redmine. Status: " + exception.getStatusCode().value(),
                    exception
            );
        } catch (Exception exception) {
            throw new RedmineIntegrationException("Nao foi possivel registrar horas no Redmine.", exception);
        }
    }

    private Long findTimeEntryActivityId(UserConfig config) {
        try {
            RedmineActivitiesResponse response = buildClient(config)
                    .get()
                    .uri("/enumerations/time_entry_activities.json")
                    .retrieve()
                    .bodyToMono(RedmineActivitiesResponse.class)
                    .block();

            if (response == null || response.timeEntryActivities() == null || response.timeEntryActivities().isEmpty()) {
                throw new RedmineIntegrationException("Nenhuma atividade de apontamento encontrada no Redmine.");
            }

            return response.timeEntryActivities()
                    .stream()
                    .filter(activity -> Boolean.TRUE.equals(activity.active()))
                    .filter(activity -> "Development".equalsIgnoreCase(activity.name()))
                    .findFirst()
                    .or(() -> response.timeEntryActivities()
                            .stream()
                            .filter(activity -> Boolean.TRUE.equals(activity.active()))
                            .filter(activity -> Boolean.TRUE.equals(activity.defaultActivity()))
                            .findFirst())
                    .or(() -> response.timeEntryActivities()
                            .stream()
                            .filter(activity -> Boolean.TRUE.equals(activity.active()))
                            .findFirst())
                    .map(RedmineActivity::id)
                    .orElseThrow(() -> new RedmineIntegrationException("Nenhuma atividade ativa encontrada no Redmine."));
        } catch (RedmineIntegrationException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new RedmineIntegrationException("Nao foi possivel buscar atividades do Redmine.", exception);
        }
    }

    private WebClient buildClient(UserConfig config) {
        return webClientBuilder
                .baseUrl(normalizeBaseUrl(config.getRedmineUrl()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader("X-Redmine-API-Key", config.getApiKey())
                .build();
    }

    private String normalizeBaseUrl(String url) {
        return Objects.requireNonNull(url).replaceAll("/+$", "");
    }
}
