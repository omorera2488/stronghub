package com.bluelitelabs.stronghub.application;

import java.util.concurrent.TimeUnit;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bluelitelabs.stronghub.web.dto.DBHealthResponse;

@Service
public class DefaultHealthService implements HealthService {

	private final JdbcTemplate jdbcTemplate;

	public DefaultHealthService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public DBHealthResponse pingDb() {
		long start = System.nanoTime();
		String status = "UP";
		String detail = null;

		try {

			jdbcTemplate.queryForObject("SELECT 1", Integer.class);
		} catch (Exception ex) {
			status = "DOWN";
			detail = ex.getClass().getSimpleName() + ": " + ex.getMessage();
		}

		long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
		return new DBHealthResponse.Builder().status(status).latencyMs(tookMs).detail(detail).build();
	}
}