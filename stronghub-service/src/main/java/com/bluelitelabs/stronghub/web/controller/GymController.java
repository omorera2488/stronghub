package com.bluelitelabs.stronghub.web.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluelitelabs.stronghub.application.GymService;
import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.web.dto.GymDto;
import com.bluelitelabs.stronghub.web.dto.PageResponse;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/gyms")
@Validated
public class GymController {

	private final GymService gymService;

	public GymController(GymService gymService) {
		this.gymService = gymService;
	}

	@GetMapping
	public PageResponse<GymDto> list(@RequestParam(required = false) @PositiveOrZero Integer page,
			@RequestParam(required = false) @Min(1) @Max(100) Integer size,
			@RequestParam(required = false, name = "sort") String[] sort) {
		return PageResponse.from(gymService.list(new PageRequestSpec(page, size, sort)));
	}
}
