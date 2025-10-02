package com.bluelitelabs.stronghub.application;

import java.time.Instant;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluelitelabs.stronghub.application.mapper.PlanMapper;
import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.application.query.Paging;
import com.bluelitelabs.stronghub.application.sort.PlanSortWhitelist;
import com.bluelitelabs.stronghub.domain.model.Plan;
import com.bluelitelabs.stronghub.infrastructure.persistence.repo.PlanRepository;
import com.bluelitelabs.stronghub.web.dto.PlanCreateRequest;
import com.bluelitelabs.stronghub.web.dto.PlanDto;
import com.bluelitelabs.stronghub.web.dto.PlanUpdateRequest;

import jakarta.validation.Valid;
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

	@Override
	@Cacheable(cacheNames = "plans:list", key = "T(java.util.Objects).hash(#spec.page,#spec.size,T(java.util.Arrays).toString(#spec.sort))")
	public Page<PlanDto> list(@Valid PageRequestSpec spec) {
		String[] safe = whitelist.sanitize(spec.getSort());
		if (safe == null || safe.length == 0)
			safe = new String[] { "name,asc", "id,asc" };
		Pageable pageable = Paging.page(spec.getPage(), spec.getSize(), safe);
		return repo.findAll(pageable).map(mapper::toDto);
	}

	@Override
	@Cacheable(cacheNames = "plans:byId", key = "#id")
	public Optional<PlanDto> findById(Long id) {
		return repo.findById(id).map(mapper::toDto);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = { "plans:list" }, allEntries = true)
	public PlanDto create(PlanCreateRequest request) {
		Plan plan = mapper.toEntity(request);
		try {
			plan = repo.save(plan);
		} catch (DataIntegrityViolationException e) {
			// Name Duplicated
			throw e;
		}
		return mapper.toDto(plan);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = { "plans:list", "plans:byId" }, allEntries = true)
	public Optional<PlanDto> update(Long id, PlanUpdateRequest request) {
		return repo.findById(id).map(entity -> {
			mapper.apply(entity, request);
			try {
				Plan saved = repo.save(entity);
				return mapper.toDto(saved);
			} catch (DataIntegrityViolationException e) {
				throw e;
			}
		});
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = { "plans:list", "plans:byId" }, allEntries = true)
	public boolean delete(Long id) {
		return repo.findById(id).map(entity -> {
			entity.setDeletedAt(Instant.now()); // soft delete
			repo.save(entity);
			return true;
		}).orElse(false);
	}
}