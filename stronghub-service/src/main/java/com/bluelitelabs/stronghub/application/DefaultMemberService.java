package com.bluelitelabs.stronghub.application;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluelitelabs.stronghub.application.mapper.MemberMapper;
import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.application.query.Paging;
import com.bluelitelabs.stronghub.application.sort.PlanSortWhitelist;
import com.bluelitelabs.stronghub.domain.model.GymMember;
import com.bluelitelabs.stronghub.domain.model.Member;
import com.bluelitelabs.stronghub.domain.model.Plan;
import com.bluelitelabs.stronghub.infrastructure.persistence.repo.GymMemberRepository;
import com.bluelitelabs.stronghub.infrastructure.persistence.repo.MemberRepository;
import com.bluelitelabs.stronghub.infrastructure.persistence.repo.PlanRepository;
import com.bluelitelabs.stronghub.web.dto.GymMemberAttachRequest;
import com.bluelitelabs.stronghub.web.dto.GymMemberDto;
import com.bluelitelabs.stronghub.web.dto.GymMemberUpdateRequest;
import com.bluelitelabs.stronghub.web.dto.MemberCreateRequest;
import com.bluelitelabs.stronghub.web.dto.MemberDto;
import com.bluelitelabs.stronghub.web.dto.MemberUpdateRequest;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class DefaultMemberService implements MemberService {
	private final MemberRepository memberRepo;
	private final GymMemberRepository gymMemberRepo;
	private final PlanRepository planRepo; // to validate plan-gym
	private final MemberMapper memberMapper;
	private final PlanSortWhitelist whitelist; // Can create one for members or reuse generic

	public DefaultMemberService(MemberRepository mr, GymMemberRepository gmr, PlanRepository pr, MemberMapper mm,
			PlanSortWhitelist wl) {
		this.memberRepo = mr;
		this.gymMemberRepo = gmr;
		this.planRepo = pr;
		this.memberMapper = mm;
		this.whitelist = wl;
	}

	// ---- Members ----
	@Override
	public Page<MemberDto> listMembers(PageRequestSpec spec) {
		String[] safe = (spec.getSort() == null || spec.getSort().length == 0)
				? new String[] { "lastName,asc", "id,asc" }
				: whitelist.sanitize(spec.getSort());
		Pageable pageable = Paging.page(spec.getPage(), spec.getSize(), safe);
		return memberRepo.findAll(pageable).map(memberMapper::toDto);
	}

	@Override
	public Optional<MemberDto> findMemberById(Long id) {
		return memberRepo.findById(id).map(memberMapper::toDto);
	}

	@Override
	@Transactional
	public MemberDto createMember(MemberCreateRequest req) {
		Member m = memberMapper.toEntity(req);
		return memberMapper.toDto(memberRepo.save(m));
	}

	@Override
	@Transactional
	public Optional<MemberDto> updateMember(Long id, MemberUpdateRequest req) {
		return memberRepo.findById(id).map(m -> {
			memberMapper.apply(m, req);
			return memberMapper.toDto(memberRepo.save(m));
		});
	}

	@Override
	@Transactional
	public boolean deleteMember(Long id) {
		return memberRepo.findById(id).map(m -> {
			memberRepo.delete(m);
			return true;
		}).orElse(false);
	}

	// ---- Gym memberships ----
	@Override
	public Page<GymMemberDto> listByGym(Long gymId, Integer page, Integer size, String[] sort) {
		String[] safe = (sort == null || sort.length == 0) ? new String[] { "memberLastName,asc", "membershipId,asc" }
				: whitelist.sanitize(sort);
		Pageable pageable = Paging.page(page, size, safe);
		return gymMemberRepo.findAllByGymIdAsDto(gymId, pageable);
	}

	@Override
	public Page<GymMemberDto> listGymMembersGlobal(PageRequestSpec spec) {
		String[] safe = (spec.getSort() == null || spec.getSort().length == 0)
				? new String[] { "gymId,asc", "memberLastName,asc" }
				: whitelist.sanitize(spec.getSort());
		Pageable pageable = Paging.page(spec.getPage(), spec.getSize(), safe);
		return gymMemberRepo.findAllAsDto(pageable);
	}

	@Override
	@Transactional
	public GymMemberDto attachToGym(Long gymId, GymMemberAttachRequest req) {
		// 1) validate that it does not already exist
		if (gymMemberRepo.existsByGymIdAndMember_Id(gymId, req.getMemberId())) {
			throw new IllegalStateException("Member ya pertenece al gym");
		}
		// 2) validate plan and that it belongs to the same gym
		Plan plan = planRepo.findById(req.getPlanId()).orElseThrow(() -> new EntityNotFoundException("Plan no existe"));
		if (!plan.getGymId().equals(gymId)) {
			throw new IllegalArgumentException("El plan no pertenece a este gym");
		}
		// 3) create membership
		Member member = memberRepo.findById(req.getMemberId())
				.orElseThrow(() -> new EntityNotFoundException("Member no existe"));
		GymMember gm = new GymMember();
		gm.setGymId(gymId);
		gm.setMember(member);
		gm.setStatus("ACTIVE");
		gm.setJoinedAt(Instant.now());
		// planId field added by migration
		gm.setPlanId(plan.getId());

		GymMember saved = gymMemberRepo.save(gm);

		GymMemberDto dto = new GymMemberDto();
		dto.setMembershipId(saved.getId());
		dto.setGymId(saved.getGymId());
		dto.setMemberId(member.getId());
		dto.setMemberFirstName(member.getFirstName());
		dto.setMemberLastName(member.getLastName());
		dto.setMemberEmail(member.getEmail());
		dto.setStatus(saved.getStatus());
		dto.setJoinedAt(saved.getJoinedAt());
		dto.setPlanId(plan.getId());
		dto.setPlanName(plan.getName());
		return dto;
	}

	@Override
	@Transactional
	public Optional<GymMemberDto> updateGymMember(Long membershipId, GymMemberUpdateRequest req) {
		return gymMemberRepo.findById(membershipId).map(gm -> {
			// validar plan pertenece al mismo gym
			Plan plan = planRepo.findById(req.getPlanId())
					.orElseThrow(() -> new EntityNotFoundException("Plan no existe"));
			if (!plan.getGymId().equals(gm.getGymId())) {
				throw new IllegalArgumentException("El plan no pertenece a este gym");
			}
			gm.setPlanId(plan.getId());
			gm.setStatus(req.getStatus());
			GymMember saved = gymMemberRepo.save(gm);

			Member m = saved.getMember();
			GymMemberDto dto = new GymMemberDto();
			dto.setMembershipId(saved.getId());
			dto.setGymId(saved.getGymId());
			dto.setMemberId(m.getId());
			dto.setMemberFirstName(m.getFirstName());
			dto.setMemberLastName(m.getLastName());
			dto.setMemberEmail(m.getEmail());
			dto.setStatus(saved.getStatus());
			dto.setJoinedAt(saved.getJoinedAt());
			dto.setPlanId(plan.getId());
			dto.setPlanName(plan.getName());
			return dto;
		});
	}

	@Override
	@Transactional
	public boolean deactivateGymMember(Long membershipId) {
		return gymMemberRepo.findById(membershipId).map(gm -> {
			gm.setStatus("INACTIVE");
			gymMemberRepo.save(gm);
			return true;
		}).orElse(false);
	}
}