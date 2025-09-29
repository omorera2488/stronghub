package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluelitelabs.stronghub.domain.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	Optional<Attendance> findByGymIdAndClassSessionIdAndMemberId(Long gymId, Long classSessionId, Long memberId);

	List<Attendance> findByGymIdAndClassSessionId(Long gymId, Long classSessionId);
}