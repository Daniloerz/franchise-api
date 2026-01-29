package com.franchises.acc.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.franchises.acc.domain.model.Branch;

public interface BranchRepository {

    Optional<Branch> findById(UUID id);

    Optional<Branch> findByIdAndFranchiseId(UUID id, UUID franchiseId);

    Branch save(Branch branch);
}

