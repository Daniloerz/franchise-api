package com.franchises.acc.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.franchises.acc.domain.model.Franchise;
import com.franchises.acc.domain.repository.FranchiseRepository;
import com.franchises.acc.infrastructure.persistence.entity.FranchiseEntity;
import com.franchises.acc.infrastructure.persistence.mapper.FranchiseMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryImpl implements FranchiseRepository {

    private final FranchiseJpaRepository franchiseJpaRepository;
    private final FranchiseMapper franchiseMapper;

    @Override
    public Optional<Franchise> findById(UUID id) {
        return franchiseJpaRepository.findById(id)
                .map(franchiseMapper::toDomain);
    }

    @Override
    public Franchise save(Franchise franchise) {
        return Optional.ofNullable(franchise)
                .map(franchiseMapper::toEntity)
                .map(franchiseJpaRepository::save)
                .map(franchiseMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Franchise must not be null"));
    }

    @Override
    public boolean existsByName(String name) {
        return franchiseJpaRepository.existsByName(name);
    }
}

