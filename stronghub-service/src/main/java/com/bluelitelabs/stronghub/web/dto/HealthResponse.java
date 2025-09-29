package com.bluelitelabs.stronghub.web.dto;

import java.util.Objects;

public class HealthResponse {
	private String status; // UP/DOWN (app)
	private String app;
	private String version;
	private String time; // ISO-8601 UTC
	private DBHealthResponse db;

	// Constructor vacío
	public HealthResponse() {
	}

	// Constructor con argumentos
	public HealthResponse(String status, String app, String version, String time, DBHealthResponse db) {
		this.status = status;
		this.app = app;
		this.version = version;
		this.time = time;
		this.db = db;
	}

	// Getters y setters
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public DBHealthResponse getDb() {
		return db;
	}

	public void setDb(DBHealthResponse db) {
		this.db = db;
	}

	// equals & hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof HealthResponse))
			return false;
		HealthResponse that = (HealthResponse) o;
		return Objects.equals(status, that.status) && Objects.equals(app, that.app)
				&& Objects.equals(version, that.version) && Objects.equals(time, that.time)
				&& Objects.equals(db, that.db);
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, app, version, time, db);
	}

	// toString
	@Override
	public String toString() {
		return "HealthResponse{" + "status='" + status + '\'' + ", app='" + app + '\'' + ", version='" + version + '\''
				+ ", time='" + time + '\'' + ", db=" + db + '}';
	}

	// Builder
	public static class Builder {
		private String status;
		private String app;
		private String version;
		private String time;
		private DBHealthResponse db;

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Builder app(String app) {
			this.app = app;
			return this;
		}

		public Builder version(String version) {
			this.version = version;
			return this;
		}

		public Builder time(String time) {
			this.time = time;
			return this;
		}

		public Builder db(DBHealthResponse db) {
			this.db = db;
			return this;
		}

		public HealthResponse build() {
			return new HealthResponse(status, app, version, time, db);
		}
	}
}
