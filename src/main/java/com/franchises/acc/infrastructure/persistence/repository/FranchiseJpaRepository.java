package com.franchises.acc.infrastructure.persistence.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.franchises.acc.infrastructure.persistence.entity.FranchiseEntity;

public interface FranchiseJpaRepository extends JpaRepository<FranchiseEntity, UUID> {

    @Override
    @EntityGraph(attributePaths = "branches")
    Optional<FranchiseEntity> findById(UUID id);

    boolean existsByName(String name);
}

