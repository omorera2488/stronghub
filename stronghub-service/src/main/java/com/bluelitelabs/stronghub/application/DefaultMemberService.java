package com.bluelitelabs.stronghub.application;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluelitelabs.stronghub.application.mapper.MemberMapper;
import com.bluelitelabs.stronghub.application.query.Paging;
import com.bluelitelabs.stronghub.application.sort.MemberSortWhitelist;
import com.bluelitelabs.stronghub.infrastructure.persistence.repo.GymMemberRepository;
import com.bluelitelabs.stronghub.web.dto.MemberDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Service
@Transactional(readOnly = true)
public class DefaultMemberService implements MemberService {
	private final GymMemberRepository gmRepo;
	private final MemberMapper mapper;
	private final MemberSortWhitelist whitelist;

	public DefaultMemberService(GymMemberRepository gmRepo, MemberMapper mapper, MemberSortWhitelist whitelist) {
		this.gmRepo = gmRepo;
		this.mapper = mapper;
		this.whitelist = whitelist;
	}

	@Override
	@Cacheable(cacheNames = "members:list:gym", key = "T(java.util.Objects).hash(#gymId,#page,#size,T(java.util.Arrays).toString(#sort))")
	public Page<MemberDto> listByGym(@NotNull @Positive Long gymId, Integer page, Integer size, String[] sort) {
		String[] safe = whitelist.sanitize(sort);
		if (safe == null || safe.length == 0)
			safe = new String[] { "lastName,asc", "firstName,asc", "id,asc" };
		Pageable pageable = Paging.page(page, size, safe);
		return gmRepo.findMembersByGymId(gymId, pageable).map(mapper::toDto);
	}
}