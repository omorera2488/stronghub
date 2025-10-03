package com.bluelitelabs.stronghub.web.dto;

import java.time.Instant;

public class GymMemberDto {

	private Long membershipId; // gymId gym_members
	private Long gymId;
	private Long memberId;
	private String memberFirstName;
	private String memberLastName;
	private String memberEmail;
	private String status; // ACTIVE/INACTIVE
	private Instant joinedAt;
	private Long planId;
	private String planName;

	public GymMemberDto() {
		// TODO Auto-generated constructor stub
	}

	public GymMemberDto(Long membershipId, Long gymId, Long memberId, String memberFirstName, String memberLastName,
			String memberEmail, String status, Instant joinedAt, Long planId, String planName) {
		super();
		this.membershipId = membershipId;
		this.gymId = gymId;
		this.memberId = memberId;
		this.memberFirstName = memberFirstName;
		this.memberLastName = memberLastName;
		this.memberEmail = memberEmail;
		this.status = status;
		this.joinedAt = joinedAt;
		this.planId = planId;
		this.planName = planName;
	}

	public Long getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(Long membershipId) {
		this.membershipId = membershipId;
	}

	public Long getGymId() {
		return gymId;
	}

	public void setGymId(Long gymId) {
		this.gymId = gymId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberFirstName() {
		return memberFirstName;
	}

	public void setMemberFirstName(String memberFirstName) {
		this.memberFirstName = memberFirstName;
	}

	public String getMemberLastName() {
		return memberLastName;
	}

	public void setMemberLastName(String memberLastName) {
		this.memberLastName = memberLastName;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Instant getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Instant joinedAt) {
		this.joinedAt = joinedAt;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

}
