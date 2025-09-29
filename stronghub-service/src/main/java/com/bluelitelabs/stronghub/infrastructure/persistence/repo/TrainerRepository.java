package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluelitelabs.stronghub.domain.model.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
	Optional<Trainer> findByGymIdAndEmail(Long gymId, String email);
}