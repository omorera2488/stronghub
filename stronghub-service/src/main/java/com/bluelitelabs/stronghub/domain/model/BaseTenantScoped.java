package com.bluelitelabs.stronghub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseTenantScoped extends BaseAuditable {
	@Column(name = "gym_id", nullable = false)
	private Long gymId;

	public Long getGymId() {
		return gymId;
	}

	public void setGymId(Long gymId) {
		this.gymId = gymId;
	}
}
