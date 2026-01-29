package com.franchises.acc.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.franchises.acc.domain.model.Product;
import com.franchises.acc.domain.repository.ProductRepository;
import com.franchises.acc.infrastructure.persistence.entity.BranchEntity;
import com.franchises.acc.infrastructure.persistence.entity.ProductEntity;
import com.franchises.acc.infrastructure.persistence.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final BranchJpaRepository branchJpaRepository;
    private final ProductMapper productMapper;

    @Override
    public Optional<Product> findById(UUID id) {
        return productJpaRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    public Optional<Product> findByIdAndBranchId(UUID id, UUID branchId) {
        return productJpaRepository.findByIdAndBranch_Id(id, branchId)
                .map(productMapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        return Optional.ofNullable(product)
                .map(Product::getBranchId)
                .flatMap(branchJpaRepository::findById)
                .map(branchEntity -> productMapper.toEntity(product, branchEntity))
                .map(productJpaRepository::save)
                .map(productMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found: " + (product != null ? product.getBranchId() : null)));
    }

    @Override
    public void deleteById(UUID id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public List<Product> findTopStockProductsByFranchiseId(UUID franchiseId) {
        return productJpaRepository.findTopStockProductsByFranchiseId(franchiseId)
                .stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }
}

