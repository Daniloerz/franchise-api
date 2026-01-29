package com.franchises.acc.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.franchises.acc.domain.model.Product;

public interface ProductRepository {

    Optional<Product> findById(UUID id);

    Optional<Product> findByIdAndBranchId(UUID id, UUID branchId);

    Product save(Product product);

    void deleteById(UUID id);

    List<Product> findTopStockProductsByFranchiseId(UUID franchiseId);
}

