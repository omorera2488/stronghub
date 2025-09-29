package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluelitelabs.stronghub.domain.enums.BookingStatus;
import com.bluelitelabs.stronghub.domain.model.ClassBooking;

public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {
	Optional<ClassBooking> findByGymIdAndClassSessionIdAndMemberId(Long gymId, Long classSessionId, Long memberId);

	List<ClassBooking> findByGymIdAndStatus(Long gymId, BookingStatus status);
}