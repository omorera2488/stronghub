package com.bluelitelabs.stronghub.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluelitelabs.stronghub.application.PlanService;
import com.bluelitelabs.stronghub.web.dto.PageResponse;
import com.bluelitelabs.stronghub.web.dto.PlanCreateRequest;
import com.bluelitelabs.stronghub.web.dto.PlanDto;
import com.bluelitelabs.stronghub.web.dto.PlanUpdateRequest;

import jakarta.validation.Valid;
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

	// GET BY ID
	@GetMapping("/{id}")
	public ResponseEntity<PlanDto> getById(@PathVariable @Positive Long id) {
		return planService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// CREATE
	@PostMapping
	public ResponseEntity<PlanDto> create(@Valid @RequestBody PlanCreateRequest request) {
		PlanDto dto = planService.create(request);
		return ResponseEntity.created(URI.create("/gyms/" + dto.getId())).body(dto);
	}

	// UPDATE
	@PutMapping("/{id}")
	public ResponseEntity<PlanDto> update(@PathVariable @Positive Long id,
			@Valid @RequestBody PlanUpdateRequest request) {
		return planService.update(id, request).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// DELETE (soft-delete)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
		boolean deleted = planService.delete(id);
		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
}
