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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluelitelabs.stronghub.application.MemberService;
import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.web.dto.GymMemberAttachRequest;
import com.bluelitelabs.stronghub.web.dto.GymMemberDto;
import com.bluelitelabs.stronghub.web.dto.GymMemberUpdateRequest;
import com.bluelitelabs.stronghub.web.dto.MemberCreateRequest;
import com.bluelitelabs.stronghub.web.dto.MemberDto;
import com.bluelitelabs.stronghub.web.dto.MemberUpdateRequest;
import com.bluelitelabs.stronghub.web.dto.PageResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@Validated
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	// ---- Members ----
	@GetMapping("/members")
	public PageResponse<MemberDto> listMembers(@RequestParam(required = false) @PositiveOrZero Integer page,
			@RequestParam(required = false) @Min(1) @Max(100) Integer size,
			@RequestParam(required = false, name = "sort") String[] sort) {
		PageRequestSpec spec = new PageRequestSpec();
		spec.setPage(page);
		spec.setSize(size);
		spec.setSort(sort);
		return PageResponse.from(memberService.listMembers(spec));
	}

	@GetMapping("/members/{id}")
	public ResponseEntity<MemberDto> getMember(@PathVariable @Positive Long id) {
		return memberService.findMemberById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/members")
	public ResponseEntity<MemberDto> createMember(@Valid @RequestBody MemberCreateRequest req) {
		MemberDto dto = memberService.createMember(req);
		return ResponseEntity.created(URI.create("/api/v1/members/" + dto.getId())).body(dto);
	}

	@PutMapping("/members/{id}")
	public ResponseEntity<MemberDto> updateMember(@PathVariable @Positive Long id,
			@Valid @RequestBody MemberUpdateRequest req) {
		return memberService.updateMember(id, req).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/members/{id}")
	public ResponseEntity<Void> deleteMember(@PathVariable @Positive Long id) {
		return memberService.deleteMember(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	// ---- Gym memberships ----
	@GetMapping("/gyms/{gymId}/members")
	public PageResponse<GymMemberDto> listByGym(@PathVariable @Positive Long gymId,
			@RequestParam(required = false) @PositiveOrZero Integer page,
			@RequestParam(required = false) @Min(1) @Max(100) Integer size,
			@RequestParam(required = false, name = "sort") String[] sort) {
		return PageResponse.from(memberService.listByGym(gymId, page, size, sort));
	}

	@PostMapping("/gyms/{gymId}/members")
	public ResponseEntity<GymMemberDto> attachToGym(@PathVariable @Positive Long gymId,
			@Valid @RequestBody GymMemberAttachRequest req) {
		GymMemberDto dto = memberService.attachToGym(gymId, req);
		return ResponseEntity.created(URI.create("/api/v1/gyms/" + gymId + "/members/" + dto.getMembershipId()))
				.body(dto);
	}

	@PutMapping("/gyms/{gymId}/members/{membershipId}")
	public ResponseEntity<GymMemberDto> updateGymMember(@PathVariable @Positive Long gymId,
			@PathVariable @Positive Long membershipId, @Valid @RequestBody GymMemberUpdateRequest req) {
		// gymId is not used in the service, but maintains the semantics of the
		// subresource
		return memberService.updateGymMember(membershipId, req).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/gyms/{gymId}/members/{membershipId}")
	public ResponseEntity<Void> deactivateGymMember(@PathVariable @Positive Long gymId,
			@PathVariable @Positive Long membershipId) {
		return memberService.deactivateGymMember(membershipId) ? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
