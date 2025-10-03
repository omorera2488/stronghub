package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bluelitelabs.stronghub.domain.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByIdAndEmail(Long id, String email);

	Optional<Member> findByEmail(String email);

	Page<Member> findById(Long id, Pageable pageable);
}
