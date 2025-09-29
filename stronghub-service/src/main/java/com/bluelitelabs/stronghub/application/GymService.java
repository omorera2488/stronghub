package com.bluelitelabs.stronghub.application;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.web.dto.GymCreateRequest;
import com.bluelitelabs.stronghub.web.dto.GymDto;
import com.bluelitelabs.stronghub.web.dto.GymUpdateRequest;

public interface GymService {
	Page<GymDto> list(PageRequestSpec spec);

	Optional<GymDto> findById(Long id);

	GymDto create(GymCreateRequest request);

	Optional<GymDto> update(Long id, GymUpdateRequest request);

	boolean delete(Long id); // soft delete
}
