package com.bluelitelabs.stronghub.application;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.web.dto.PlanCreateRequest;
import com.bluelitelabs.stronghub.web.dto.PlanDto;
import com.bluelitelabs.stronghub.web.dto.PlanUpdateRequest;

public interface PlanService {
	Page<PlanDto> listByGym(Long gymId, Integer page, Integer size, String[] sort);

	Page<PlanDto> list(PageRequestSpec spec);

	Optional<PlanDto> findById(Long id);

	PlanDto create(PlanCreateRequest request);

	Optional<PlanDto> update(Long id, PlanUpdateRequest request);

	boolean delete(Long id); // soft delete
}
