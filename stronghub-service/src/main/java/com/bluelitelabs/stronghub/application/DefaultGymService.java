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

import com.bluelitelabs.stronghub.application.mapper.GymMapper;
import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.application.query.Paging;
import com.bluelitelabs.stronghub.application.sort.GymSortWhitelist;
import com.bluelitelabs.stronghub.domain.model.Gym;
import com.bluelitelabs.stronghub.infrastructure.persistence.repo.GymRepository;
import com.bluelitelabs.stronghub.web.dto.GymCreateRequest;
import com.bluelitelabs.stronghub.web.dto.GymDto;
import com.bluelitelabs.stronghub.web.dto.GymUpdateRequest;

import jakarta.validation.Valid;

@Service
@Transactional(readOnly = true)
public class DefaultGymService implements GymService {
	private final GymRepository repo;
	private final GymMapper mapper;
	private final GymSortWhitelist whitelist;

	public DefaultGymService(GymRepository repo, GymMapper mapper, GymSortWhitelist whitelist) {
		this.repo = repo;
		this.mapper = mapper;
		this.whitelist = whitelist;
	}

	@Override
	@Cacheable(cacheNames = "gyms:list", key = "T(java.util.Objects).hash(#spec.page,#spec.size,T(java.util.Arrays).toString(#spec.sort))")
	public Page<GymDto> list(@Valid PageRequestSpec spec) {
		String[] safe = whitelist.sanitize(spec.getSort());
		if (safe == null || safe.length == 0)
			safe = new String[] { "name,asc", "id,asc" };
		Pageable pageable = Paging.page(spec.getPage(), spec.getSize(), safe);
		return repo.findAll(pageable).map(mapper::toDto);
	}

	@Override
	@Cacheable(cacheNames = "gyms:byId", key = "#id")
	public Optional<GymDto> findById(Long id) {
		return repo.findById(id).map(mapper::toDto);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = { "gyms:list" }, allEntries = true)
	public GymDto create(GymCreateRequest request) {
		Gym g = mapper.toEntity(request);
		try {
			g = repo.save(g);
		} catch (DataIntegrityViolationException e) {
			// ej: nombre duplicado (índice único)
			throw e;
		}
		return mapper.toDto(g);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = { "gyms:list", "gyms:byId" }, allEntries = true)
	public Optional<GymDto> update(Long id, GymUpdateRequest request) {
		return repo.findById(id).map(entity -> {
			mapper.apply(entity, request);
			try {
				Gym saved = repo.save(entity);
				return mapper.toDto(saved);
			} catch (DataIntegrityViolationException e) {
				throw e;
			}
		});
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = { "gyms:list", "gyms:byId" }, allEntries = true)
	public boolean delete(Long id) {
		return repo.findById(id).map(entity -> {
			entity.setDeletedAt(Instant.now()); // soft delete
			repo.save(entity);
			return true;
		}).orElse(false);
	}
}
