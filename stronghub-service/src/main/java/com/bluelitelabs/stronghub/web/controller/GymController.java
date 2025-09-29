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

import com.bluelitelabs.stronghub.application.GymService;
import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.web.dto.GymCreateRequest;
import com.bluelitelabs.stronghub.web.dto.GymDto;
import com.bluelitelabs.stronghub.web.dto.GymUpdateRequest;
import com.bluelitelabs.stronghub.web.dto.PageResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/gyms")
@Validated
public class GymController {

	private final GymService gymService;

	public GymController(GymService gymService) {
		this.gymService = gymService;
	}

	// LIST
	@GetMapping
	public PageResponse<GymDto> list(@RequestParam(required = false) @PositiveOrZero Integer page,
			@RequestParam(required = false) @Min(1) @Max(100) Integer size,
			@RequestParam(required = false, name = "sort") String[] sort) {
		return PageResponse.from(gymService.list(new PageRequestSpec(page, size, sort)));
	}

	// GET BY ID
	@GetMapping("/{id}")
	public ResponseEntity<GymDto> getById(@PathVariable @Positive Long id) {
		return gymService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// CREATE
	@PostMapping
	public ResponseEntity<GymDto> create(@Valid @RequestBody GymCreateRequest request) {
		GymDto dto = gymService.create(request);
		return ResponseEntity.created(URI.create("/gyms/" + dto.getId())).body(dto);
	}

	// UPDATE
	@PutMapping("/{id}")
	public ResponseEntity<GymDto> update(@PathVariable @Positive Long id,
			@Valid @RequestBody GymUpdateRequest request) {
		return gymService.update(id, request).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// DELETE (soft-delete)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
		boolean deleted = gymService.delete(id);
		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
}
