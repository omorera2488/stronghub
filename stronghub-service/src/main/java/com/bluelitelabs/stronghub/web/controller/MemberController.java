package com.bluelitelabs.stronghub.web.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluelitelabs.stronghub.application.MemberService;
import com.bluelitelabs.stronghub.web.dto.MemberDto;
import com.bluelitelabs.stronghub.web.dto.PageResponse;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/members")
@Validated
public class MemberController {

	private final MemberService service;

	public MemberController(MemberService service) {
		this.service = service;
	}

	@GetMapping
	public PageResponse<MemberDto> list(@RequestParam @NotNull @Positive Long gymId,
			@RequestParam(required = false) @PositiveOrZero Integer page,
			@RequestParam(required = false) @Min(1) @Max(100) Integer size,
			@RequestParam(required = false, name = "sort") String[] sort) {
		return PageResponse.from(service.listByGym(gymId, page, size, sort));
	}
}
