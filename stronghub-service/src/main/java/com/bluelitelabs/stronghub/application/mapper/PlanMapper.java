package com.bluelitelabs.stronghub.application.mapper;

import org.springframework.stereotype.Component;

import com.bluelitelabs.stronghub.domain.model.Plan;
import com.bluelitelabs.stronghub.web.dto.PlanCreateRequest;
import com.bluelitelabs.stronghub.web.dto.PlanDto;
import com.bluelitelabs.stronghub.web.dto.PlanUpdateRequest;

@Component
public class PlanMapper {
	public PlanDto toDto(Plan p) {
		return new PlanDto(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getDurationDays(), true);
	}

	public Plan toEntity(PlanCreateRequest planRequest) {
		Plan plan = new Plan();
		plan.setGymId(planRequest.getGymId());
		plan.setName(planRequest.getName());
		plan.setDescription(planRequest.getDescription());
		plan.setPrice(planRequest.getPrice());
		plan.setDurationDays(planRequest.getDurationDays());
		return plan;
	}

	public void apply(Plan plan, PlanUpdateRequest planRequest) {
		plan.setGymId(planRequest.getGymId());
		plan.setName(planRequest.getName());
		plan.setDescription(planRequest.getDescription());
		plan.setPrice(planRequest.getPrice());
		plan.setDurationDays(planRequest.getDurationDays());
	}
}
