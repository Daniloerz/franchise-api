package com.franchises.acc.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.franchises.acc.application.service.FranchiseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.franchises.acc.application.dto.request.CreateFranchiseRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.response.FranchiseResponse;
import com.franchises.acc.application.dto.response.TopStockProductResponse;
import com.franchises.acc.domain.exception.DuplicateResourceException;
import com.franchises.acc.domain.exception.ResourceNotFoundException;
import com.franchises.acc.domain.model.Branch;
import com.franchises.acc.domain.model.Franchise;
import com.franchises.acc.domain.model.Product;
import com.franchises.acc.domain.repository.BranchRepository;
import com.franchises.acc.domain.repository.FranchiseRepository;
import com.franchises.acc.domain.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public FranchiseResponse createFranchise(CreateFranchiseRequest request) {
        if (franchiseRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Franchise", "name", request.name());
        }

        LocalDateTime now = LocalDateTime.now();
        Franchise franchise = Franchise.builder()
                .name(request.name())
                .branches(List.of())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Franchise saved = franchiseRepository.save(franchise);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public FranchiseResponse updateFranchiseName(UUID franchiseId, UpdateNameRequest request) {
        Franchise franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new ResourceNotFoundException("Franchise", "id", franchiseId));

        franchise.setName(request.name());
        franchise.setUpdatedAt(LocalDateTime.now());

        Franchise saved = franchiseRepository.save(franchise);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopStockProductResponse> getTopStockProductsByFranchise(UUID franchiseId) {
        franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new ResourceNotFoundException("Franchise", "id", franchiseId));

        List<Product> products = productRepository.findTopStockProductsByFranchiseId(franchiseId);

        return products.stream()
                .map(product -> {
                    Branch branch = branchRepository.findById(product.getBranchId())
                            .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", product.getBranchId()));
                    return toTopStockResponse(product, branch);
                })
                .toList();
    }

    private FranchiseResponse toResponse(Franchise franchise) {
        return new FranchiseResponse(
                franchise.getId(),
                franchise.getName(),
                franchise.getCreatedAt()
        );
    }

    private TopStockProductResponse toTopStockResponse(Product product, Branch branch) {
        return new TopStockProductResponse(
                product.getId(),
                product.getName(),
                product.getStock(),
                branch.getId(),
                branch.getName()
        );
    }
}

