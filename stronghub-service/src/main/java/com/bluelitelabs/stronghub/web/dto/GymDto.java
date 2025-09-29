package com.bluelitelabs.stronghub.web.dto;

public class GymDto {
	private Long id;
	private String name;
	private String country;
	private String currency;
	private String status;
	private String settings;

	public GymDto() {
	}

	public GymDto(Long id, String name, String country, String currency, String status, String settings) {
		this.id = id;
		this.name = name;
		this.country = country;
		this.currency = currency;
		this.status = status;
		this.settings = settings;
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

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

}
