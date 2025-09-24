package com.bluelitelabs.stronghub.dto;

import java.util.Objects;

public class DBHealthResponse {
	private String status; // UP/DOWN (db)
	private Long latencyMs;
	private String detail; // null cuando UP

	// Constructor vacío
	public DBHealthResponse() {
	}

	// Constructor con argumentos
	public DBHealthResponse(String status, Long latencyMs, String detail) {
		this.status = status;
		this.latencyMs = latencyMs;
		this.detail = detail;
	}

	// Getters y setters
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getLatencyMs() {
		return latencyMs;
	}

	public void setLatencyMs(Long latencyMs) {
		this.latencyMs = latencyMs;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	// equals & hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DBHealthResponse))
			return false;
		DBHealthResponse that = (DBHealthResponse) o;
		return Objects.equals(status, that.status) && Objects.equals(latencyMs, that.latencyMs)
				&& Objects.equals(detail, that.detail);
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, latencyMs, detail);
	}

	// toString
	@Override
	public String toString() {
		return "DBHealthResponse{" + "status='" + status + '\'' + ", latencyMs=" + latencyMs + ", detail='" + detail
				+ '\'' + '}';
	}

	// Builder
	public static class Builder {
		private String status;
		private Long latencyMs;
		private String detail;

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Builder latencyMs(Long latencyMs) {
			this.latencyMs = latencyMs;
			return this;
		}

		public Builder detail(String detail) {
			this.detail = detail;
			return this;
		}

		public DBHealthResponse build() {
			return new DBHealthResponse(status, latencyMs, detail);
		}
	}
}
