package com.bluelitelabs.stronghub.application;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.bluelitelabs.stronghub.application.query.PageRequestSpec;
import com.bluelitelabs.stronghub.web.dto.GymMemberAttachRequest;
import com.bluelitelabs.stronghub.web.dto.GymMemberDto;
import com.bluelitelabs.stronghub.web.dto.GymMemberUpdateRequest;
import com.bluelitelabs.stronghub.web.dto.MemberCreateRequest;
import com.bluelitelabs.stronghub.web.dto.MemberDto;
import com.bluelitelabs.stronghub.web.dto.MemberUpdateRequest;

public interface MemberService {
	// People (members table)
	Page<MemberDto> listMembers(PageRequestSpec spec);

	Optional<MemberDto> findMemberById(Long id);

	MemberDto createMember(MemberCreateRequest req);

	Optional<MemberDto> updateMember(Long id, MemberUpdateRequest req);

	boolean deleteMember(Long id); // if allow deleting the person

	// Gym memberships
	Page<GymMemberDto> listByGym(Long gymId, Integer page, Integer size, String[] sort);

	Page<GymMemberDto> listGymMembersGlobal(PageRequestSpec spec); // admin

	GymMemberDto attachToGym(Long gymId, GymMemberAttachRequest req);

	Optional<GymMemberDto> updateGymMember(Long membershipId, GymMemberUpdateRequest req);

	boolean deactivateGymMember(Long membershipId); // status=INACTIVE
}
