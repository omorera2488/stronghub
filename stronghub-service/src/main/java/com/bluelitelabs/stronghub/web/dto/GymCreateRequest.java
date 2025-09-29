package com.bluelitelabs.stronghub.web.dto;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GymCreateRequest {

	@NotBlank
	@Size(max = 120)
	private String name;
	@Size(max = 2)
	private String country; // ISO-2 (opcional)
	@Size(max = 3)
	private String currency; // ISO-3 (opcional)
	@Size(max = 20)
	private String status; // ACTIVE/INACTIVE/etc
	// JSON como string; si luego quieres tiparlo, hacemos un Converter
	private JsonNode settings;

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
