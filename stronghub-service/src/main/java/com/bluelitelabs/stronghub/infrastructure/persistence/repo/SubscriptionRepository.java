package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluelitelabs.stronghub.domain.enums.SubscriptionStatus;
import com.bluelitelabs.stronghub.domain.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	List<Subscription> findByGymIdAndMemberIdAndStatus(Long gymId, Long memberId, SubscriptionStatus status);

	List<Subscription> findByGymIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long gymId, LocalDate date1,
			LocalDate date2);
}
