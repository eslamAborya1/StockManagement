package com.stockpilot.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * API response representation for RFC 9457 Problem Details errors.
 * Note: This is an API DTO, not a domain entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetailsResponse(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        Instant timestamp,
        List<InvalidParam> invalidParams
) {
    public record InvalidParam(
            String field,
            String reason
    ) {}
}
