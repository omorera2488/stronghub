package com.bluelitelabs.stronghub.application;

import org.springframework.data.domain.Page;

import com.bluelitelabs.stronghub.web.dto.PlanDto;

public interface PlanService {
	Page<PlanDto> listByGym(Long gymId, Integer page, Integer size, String[] sort);
}
