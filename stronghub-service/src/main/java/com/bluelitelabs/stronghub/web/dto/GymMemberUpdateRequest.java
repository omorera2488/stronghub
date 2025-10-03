package com.bluelitelabs.stronghub.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class GymMemberUpdateRequest {
	@NotNull
	@Positive
	private Long planId;
	@NotBlank
	private String status; // ACTIVE/INACTIVE

	public GymMemberUpdateRequest(@NotNull @Positive Long planId, @NotBlank String status) {
		super();
		this.planId = planId;
		this.status = status;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
