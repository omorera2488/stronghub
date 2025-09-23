package com.bluelitelabs.stronghub.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DBHealthResponse {
	private String status; // UP/DOWN (db)
	private Long latencyMs;
	private String detail; // null cuando UP
}
