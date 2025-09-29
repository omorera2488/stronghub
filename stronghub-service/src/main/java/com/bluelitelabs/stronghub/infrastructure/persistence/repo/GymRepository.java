package com.bluelitelabs.stronghub.infrastructure.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluelitelabs.stronghub.domain.model.Gym;

public interface GymRepository extends JpaRepository<Gym, Long> {
}
