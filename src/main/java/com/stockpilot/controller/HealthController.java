package com.stockpilot.api.controller;

import com.stockpilot.api.dto.HealthStatusResponse;
import com.stockpilot.api.dto.HealthStatusResponse.ComponentStatus;
import com.stockpilot.api.dto.ProblemDetailsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@Tag(name = "Health", description = "System operational health monitoring endpoints")
public class HealthController {

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Operation(summary = "System Health Check", description = "Reports application operational status and database connectivity.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "System operational and database connected",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HealthStatusResponse.class))
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "System or database connectivity degraded/unreachable",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetailsResponse.class))
            )
    })
    @GetMapping("/health")
    public ResponseEntity<HealthStatusResponse> getHealth() {
        boolean dbHealthy = isDatabaseConnected();
        String overallStatus = dbHealthy ? "UP" : "DOWN";

        Map<String, ComponentStatus> components = new HashMap<>();
        components.put("database", new ComponentStatus(dbHealthy ? "UP" : "DOWN"));

        HealthStatusResponse response = new HealthStatusResponse(overallStatus, components);

        if (dbHealthy) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }

    private boolean isDatabaseConnected() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}
