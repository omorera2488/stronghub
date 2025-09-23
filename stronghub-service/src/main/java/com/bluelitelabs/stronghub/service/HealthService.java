package com.bluelitelabs.stronghub.service;

import com.bluelitelabs.stronghub.dto.DBHealthResponse;

public interface HealthService {
	DBHealthResponse pingDb();
}
