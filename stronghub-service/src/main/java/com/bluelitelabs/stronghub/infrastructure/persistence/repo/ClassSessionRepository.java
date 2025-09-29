package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluelitelabs.stronghub.domain.model.ClassSession;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
	List<ClassSession> findByGymIdAndStartAtBetween(Long gymId, Instant start, Instant end);
}
