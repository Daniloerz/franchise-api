package com.franchises.acc.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.franchises.acc.domain.model.Branch;
import com.franchises.acc.domain.repository.BranchRepository;
import com.franchises.acc.infrastructure.persistence.entity.BranchEntity;
import com.franchises.acc.infrastructure.persistence.entity.FranchiseEntity;
import com.franchises.acc.infrastructure.persistence.mapper.BranchMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryImpl implements BranchRepository {

    private final BranchJpaRepository branchJpaRepository;
    private final FranchiseJpaRepository franchiseJpaRepository;
    private final BranchMapper branchMapper;

    @Override
    public Optional<Branch> findById(UUID id) {
        return branchJpaRepository.findById(id)
                .map(branchMapper::toDomain);
    }

    @Override
    public Optional<Branch> findByIdAndFranchiseId(UUID id, UUID franchiseId) {
        return branchJpaRepository.findByIdAndFranchise_Id(id, franchiseId)
                .map(branchMapper::toDomain);
    }

    @Override
    public Branch save(Branch branch) {
        return Optional.ofNullable(branch)
                .map(Branch::getFranchiseId)
                .flatMap(franchiseJpaRepository::findById)
                .map(franchiseEntity -> branchMapper.toEntity(branch, franchiseEntity))
                .map(branchJpaRepository::save)
                .map(branchMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Franchise not found: " + (branch != null ? branch.getFranchiseId() : null)));
    }
}

