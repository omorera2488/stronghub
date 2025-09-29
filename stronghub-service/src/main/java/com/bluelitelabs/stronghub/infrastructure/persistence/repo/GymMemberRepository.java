package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bluelitelabs.stronghub.domain.model.GymMember;
import com.bluelitelabs.stronghub.domain.model.Member;

public interface GymMemberRepository extends JpaRepository<GymMember, Long> {
	@Query("select m from Member m " + "join GymMember gm on gm.member = m " + "where gm.gymId = :gymId "
			+ "and gm.deletedAt is null and m.deletedAt is null")
	Page<Member> findMembersByGymId(@Param("gymId") Long gymId, Pageable pageable);
}
