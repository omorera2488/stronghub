package com.bluelitelabs.stronghub.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class GymMemberAttachRequest {
	@NotNull
	@Positive
	private Long memberId;
	@NotNull
	@Positive
	private Long planId;

	public GymMemberAttachRequest(@NotNull @Positive Long memberId, @NotNull @Positive Long planId) {
		super();
		this.memberId = memberId;
		this.planId = planId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

}
