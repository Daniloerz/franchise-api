package com.franchises.acc.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.franchises.acc.application.service.BranchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.franchises.acc.application.dto.request.CreateBranchRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.BranchResponse;
import com.franchises.acc.domain.exception.ResourceNotFoundException;
import com.franchises.acc.domain.model.Branch;
import com.franchises.acc.domain.repository.BranchRepository;
import com.franchises.acc.domain.repository.FranchiseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public BranchResponse addBranchToFranchise(UUID franchiseId, CreateBranchRequest request) {
        franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new ResourceNotFoundException("Franchise", "id", franchiseId));

        LocalDateTime now = LocalDateTime.now();
        Branch branch = Branch.builder()
                .name(request.name())
                .franchiseId(franchiseId)
                .products(List.of())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Branch saved = branchRepository.save(branch);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public BranchResponse updateBranchName(UUID franchiseId, UUID branchId, UpdateNameRequest request) {
        Branch branch = branchRepository.findByIdAndFranchiseId(branchId, franchiseId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));

        branch.setName(request.name());
        branch.setUpdatedAt(LocalDateTime.now());

        Branch saved = branchRepository.save(branch);
        return toResponse(saved);
    }

    private BranchResponse toResponse(Branch branch) {
        return new BranchResponse(
                branch.getId(),
                branch.getName(),
                branch.getFranchiseId(),
                branch.getCreatedAt()
        );
    }
}

