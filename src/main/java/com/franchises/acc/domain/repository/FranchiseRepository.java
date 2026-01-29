package com.franchises.acc.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.franchises.acc.domain.model.Franchise;

public interface FranchiseRepository {

    Optional<Franchise> findById(UUID id);

    Franchise save(Franchise franchise);

    boolean existsByName(String name);
}

