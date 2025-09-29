package com.bluelitelabs.stronghub.domain.model;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "gyms", indexes = { @Index(name = "ix_gym_name", columnList = "name", unique = true),
		@Index(name = "ix_gym_country", columnList = "country"),
		@Index(name = "ix_gym_status", columnList = "status") })
@SQLDelete(sql = "UPDATE gyms SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Gym extends BaseAuditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 150)
	private String name;

	@Column(name = "country", length = 5, nullable = false)
	private String country;

	@Column(name = "currency", length = 5, nullable = false)
	private String currency;

	@Column(name = "status", length = 20, nullable = false)
	private String status;

	@JdbcTypeCode(SqlTypes.JSON) // 👈 native JSONB
	@Column(name = "settings", columnDefinition = "jsonb")
	private JsonNode settings;

	public Gym() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public JsonNode getSettings() {
		return settings;
	}

	public void setSettings(JsonNode settings) {
		this.settings = settings;
	}

}
