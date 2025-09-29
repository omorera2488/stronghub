package com.bluelitelabs.stronghub.application;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluelitelabs.stronghub.application.mapper.PlanMapper;
import com.bluelitelabs.stronghub.application.query.Paging;
import com.bluelitelabs.stronghub.application.sort.PlanSortWhitelist;
import com.bluelitelabs.stronghub.infrastructure.persistence.repo.PlanRepository;
import com.bluelitelabs.stronghub.web.dto.PlanDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Service
@Transactional(readOnly = true)
public class DefaultPlanService implements PlanService {
	private final PlanRepository repo;
	private final PlanMapper mapper;
	private final PlanSortWhitelist whitelist;

	public DefaultPlanService(PlanRepository repo, PlanMapper mapper, PlanSortWhitelist whitelist) {
		this.repo = repo;
		this.mapper = mapper;
		this.whitelist = whitelist;
	}

	@Override
	@Cacheable(cacheNames = "plans:list:gym", key = "T(java.util.Objects).hash(#gymId,#page,#size,T(java.util.Arrays).toString(#sort))")
	public Page<PlanDto> listByGym(@NotNull @Positive Long gymId, Integer page, Integer size, String[] sort) {
		String[] safe = whitelist.sanitize(sort);
		if (safe == null || safe.length == 0)
			safe = new String[] { "name,asc", "id,asc" };
		Pageable pageable = Paging.page(page, size, safe);
		return repo.findByGymId(gymId, pageable).map(mapper::toDto);
	}
}