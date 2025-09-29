package com.bluelitelabs.stronghub.application;

import com.bluelitelabs.stronghub.web.dto.DBHealthResponse;

public interface HealthService {
	DBHealthResponse pingDb();
}
