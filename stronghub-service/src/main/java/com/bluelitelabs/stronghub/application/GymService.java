package com.bluelitelabs.stronghub.application;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.web.dto.GymDto;

public interface GymService {
	Page<GymDto> list(PageRequestSpec spec);

	Optional<GymDto> findById(Long id);
}
