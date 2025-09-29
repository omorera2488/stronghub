package com.bluelitelabs.stronghub.application.mapper;

import org.springframework.stereotype.Component;

import com.bluelitelabs.stronghub.domain.model.Plan;
import com.bluelitelabs.stronghub.web.dto.PlanDto;

@Component
public class PlanMapper {
	public PlanDto toDto(Plan p) {
		return new PlanDto(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getDurationDays(), true);
	}
}
