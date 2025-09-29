package com.bluelitelabs.stronghub.application;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluelitelabs.stronghub.application.mapper.GymMapper;
import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.application.query.Paging;
import com.bluelitelabs.stronghub.application.sort.GymSortWhitelist;
import com.bluelitelabs.stronghub.infrastructure.persistence.repo.GymRepository;
import com.bluelitelabs.stronghub.web.dto.GymDto;

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
}
