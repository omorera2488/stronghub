package com.bluelitelabs.stronghub.application.mapper;

import org.springframework.stereotype.Component;

import com.bluelitelabs.stronghub.domain.model.Plan;
import com.bluelitelabs.stronghub.web.dto.PlanCreateRequest;
import com.bluelitelabs.stronghub.web.dto.PlanDto;
import com.bluelitelabs.stronghub.web.dto.PlanUpdateRequest;

@Component
public class PlanMapper {
	public PlanDto toDto(Plan p) {
		boolean active = (p.getDeletedAt() == null);
		return new PlanDto(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getDurationDays(), active,
				p.getGymId(), null);
	}

	public Plan toEntity(PlanCreateRequest req) {
		Plan plan = new Plan();
		plan.setGymId(req.getGymId());
		plan.setName(req.getName());
		plan.setDescription(req.getDescription());
		plan.setPrice(req.getPrice());
		plan.setDurationDays(req.getDurationDays());
		return plan;
	}

	public void apply(Plan plan, PlanUpdateRequest req) {
		plan.setGymId(req.getGymId());
		plan.setName(req.getName());
		plan.setDescription(req.getDescription());
		plan.setPrice(req.getPrice());
		plan.setDurationDays(req.getDurationDays());
	}
}