package com.bluelitelabs.stronghub.application;

import org.springframework.data.domain.Page;

import com.bluelitelabs.stronghub.web.dto.MemberDto;

public interface MemberService {
	Page<MemberDto> listByGym(Long gymId, Integer page, Integer size, String[] sort);
}
