package com.stockpilot.api.dto;

import java.util.Map;

/**
 * API response representation for the GET /health system readiness endpoint.
 * Note: This is an API DTO, not a domain entity.
 */
public record HealthStatusResponse(
        String status,
        Map<String, ComponentStatus> components
) {
    public record ComponentStatus(
            String status
    ) {}
}
