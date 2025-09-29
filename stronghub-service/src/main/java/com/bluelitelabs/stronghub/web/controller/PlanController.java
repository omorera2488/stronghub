package com.bluelitelabs.stronghub.web.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluelitelabs.stronghub.application.PlanService;
import com.bluelitelabs.stronghub.web.dto.PageResponse;
import com.bluelitelabs.stronghub.web.dto.PlanDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/plans")
@Validated
public class PlanController {

	private final PlanService planService;

	public PlanController(PlanService planService) {
		this.planService = planService;
	}

	@GetMapping
	public PageResponse<PlanDto> list(@RequestParam @NotNull @Positive Long gymId,
			@RequestParam(required = false) @PositiveOrZero Integer page,
			@RequestParam(required = false) @Min(1) @Max(100) Integer size,
			@RequestParam(required = false, name = "sort") String[] sort) {
		return PageResponse.from(planService.listByGym(gymId, page, size, sort));
	}
}
