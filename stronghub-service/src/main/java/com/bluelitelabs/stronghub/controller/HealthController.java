package com.bluelitelabs.stronghub.controller;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluelitelabs.stronghub.dto.DBHealthResponse;
import com.bluelitelabs.stronghub.dto.HealthResponse;
import com.bluelitelabs.stronghub.service.HealthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Service & DB health checks")
public class HealthController {

	@Autowired
	private HealthService healthService;

	@Value("${spring.application.name:stronghub-service}")
	private String appName;

	@Value("${app.version:0.0.1-SNAPSHOT}")
	private String appVersion;

	@Operation(summary = "App health", description = "Returns app status and DB ping")
	@GetMapping
	public ResponseEntity<HealthResponse> health() {
		DBHealthResponse db = healthService.pingDb();
		HealthResponse resp = new HealthResponse.Builder().status("UP").app(appName).version(appVersion)
				.time(OffsetDateTime.now(ZoneOffset.UTC).toString()).db(db).build();

		return ResponseEntity.ok(resp);
	}

	@Operation(summary = "DB health", description = "Pings database and returns latency (ms)")
	@GetMapping("/db")
	public ResponseEntity<DBHealthResponse> db() {
		return ResponseEntity.ok(healthService.pingDb());
	}
}
