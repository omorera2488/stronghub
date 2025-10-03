package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bluelitelabs.stronghub.domain.model.Plan;
import com.bluelitelabs.stronghub.web.dto.PlanDto;

public interface PlanRepository extends JpaRepository<Plan, Long> {

	@Query("""
			select new com.bluelitelabs.stronghub.web.dto.PlanDto(
			  p.id, p.name, p.description, p.price, p.durationDays,
			  (p.deletedAt is null), p.gymId,
			  g.name
			)
			from Plan p
			left join Gym g on g.id = p.gymId
			where p.gymId = :gymId
			""")
	Page<PlanDto> findAllByGymIdAsDto(@Param("gymId") Long gymId, Pageable pageable);

	@Query("""
			select new com.bluelitelabs.stronghub.web.dto.PlanDto(
			  p.id, p.name, p.description, p.price, p.durationDays,
			  (p.deletedAt is null), p.gymId,
			  g.name
			)
			from Plan p
			left join Gym g on g.id = p.gymId
			""")
	Page<PlanDto> findAllAsDto(Pageable pageable);
}
