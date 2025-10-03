package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bluelitelabs.stronghub.domain.model.GymMember;
import com.bluelitelabs.stronghub.domain.model.Member;
import com.bluelitelabs.stronghub.web.dto.GymMemberDto;

public interface GymMemberRepository extends JpaRepository<GymMember, Long> {
	@Query("select m from Member m " + "join GymMember gm on gm.member = m " + "where gm.gymId = :gymId "
			+ "and gm.deletedAt is null and m.deletedAt is null")
	Page<Member> findMembersByGymId(@Param("gymId") Long gymId, Pageable pageable);

	// List of gym members (sub-resource)
	@Query("""
			  select new com.bluelitelabs.stronghub.web.dto.GymMemberDto(
			    gm.id, gm.gymId, m.id,
			    m.firstName, m.lastName, m.email,
			    gm.status, gm.joinedAt,
			    p.id, p.name
			  )
			  from GymMember gm
			  join gm.member m
			  left join Plan p on p.id = gm.planId
			  where gm.gymId = :gymId
			""")
	Page<GymMemberDto> findAllByGymIdAsDto(@Param("gymId") Long gymId, Pageable pageable);

	// Global list (multi-gym, admin only)
	@Query("""
			  select new com.bluelitelabs.stronghub.web.dto.GymMemberDto(
			    gm.id, gm.gymId, m.id,
			    m.firstName, m.lastName, m.email,
			    gm.status, gm.joinedAt,
			    p.id, p.name
			  )
			  from GymMember gm
			  join gm.member m
			  left join Plan p on p.id = gm.planId
			""")
	Page<GymMemberDto> findAllAsDto(Pageable pageable);

	boolean existsByGymIdAndMember_Id(Long gymId, Long memberId);
}
