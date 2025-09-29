package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bluelitelabs.stronghub.domain.model.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {

	Page<Plan> findByGymId(Long gymId, Pageable pageable);
}
