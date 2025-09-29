package com.bluelitelabs.stronghub.application.mapper;

import org.springframework.stereotype.Component;

import com.bluelitelabs.stronghub.domain.model.Gym;
import com.bluelitelabs.stronghub.web.dto.GymCreateRequest;
import com.bluelitelabs.stronghub.web.dto.GymDto;
import com.bluelitelabs.stronghub.web.dto.GymUpdateRequest;

@Component
public class GymMapper {
	public GymDto toDto(Gym g) {
		return new GymDto(g.getId(), g.getName(), g.getCountry(), g.getCurrency(), g.getStatus(), g.getSettings());
	}

	public Gym toEntity(GymCreateRequest r) {
		Gym g = new Gym();
		g.setName(r.getName());
		g.setCountry(r.getCountry());
		g.setCurrency(r.getCurrency());
		g.setStatus(r.getStatus() != null ? r.getStatus() : "ACTIVE");
		g.setSettings(r.getSettings());
		return g;
	}

	public void apply(Gym g, GymUpdateRequest r) {
		g.setName(r.getName());
		g.setCountry(r.getCountry());
		g.setCurrency(r.getCurrency());
		g.setStatus(r.getStatus());
		g.setSettings(r.getSettings());
	}
}
