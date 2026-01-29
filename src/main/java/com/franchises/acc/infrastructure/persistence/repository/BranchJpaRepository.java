package com.franchises.acc.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.franchises.acc.infrastructure.persistence.entity.BranchEntity;

public interface BranchJpaRepository extends JpaRepository<BranchEntity, UUID> {

    Optional<BranchEntity> findByIdAndFranchise_Id(UUID id, UUID franchiseId);
}

