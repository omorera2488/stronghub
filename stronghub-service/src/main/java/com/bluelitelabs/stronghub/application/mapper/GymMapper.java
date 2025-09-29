package com.bluelitelabs.stronghub.application.mapper;

import org.springframework.stereotype.Component;

import com.bluelitelabs.stronghub.domain.model.Gym;
import com.bluelitelabs.stronghub.web.dto.GymDto;

@Component
public class GymMapper {
	public GymDto toDto(Gym g) {
		return new GymDto(g.getId(), g.getName(), g.getCountry(), g.getCurrency(), g.getStatus(), g.getSettings());
	}
}
