package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluelitelabs.stronghub.domain.enums.PaymentStatus;
import com.bluelitelabs.stronghub.domain.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Optional<Payment> findByGymIdAndExternalRef(Long gymId, String externalRef);

	List<Payment> findByGymIdAndStatus(Long gymId, PaymentStatus status);
}
