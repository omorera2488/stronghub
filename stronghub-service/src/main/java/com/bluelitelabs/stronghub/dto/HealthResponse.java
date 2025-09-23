package com.bluelitelabs.stronghub.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthResponse {
	private String status; // UP/DOWN (app)
	private String app;
	private String version;
	private String time; // ISO-8601 UTC
	private DBHealthResponse db;
}
