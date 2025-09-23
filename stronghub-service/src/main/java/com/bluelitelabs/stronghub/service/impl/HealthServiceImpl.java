package com.bluelitelabs.stronghub.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bluelitelabs.stronghub.dto.DBHealthResponse;
import com.bluelitelabs.stronghub.service.HealthService;

@Service
public class HealthServiceImpl implements HealthService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public DBHealthResponse pingDb() {
		long start = System.nanoTime();
		String status = "DOWN";
		String detail = null;

		try {
			Integer one = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
			if (one != null && one == 1) {
				status = "UP";
			} else {
				detail = "Unexpected result";
			}
		} catch (Exception ex) {
			detail = ex.getClass().getSimpleName() + ": " + ex.getMessage();
		}
		long elapsedMs = (System.nanoTime() - start) / 1_000_000L;

		return DBHealthResponse.builder().status(status).latencyMs(elapsedMs).detail(detail).build();
	}
}
