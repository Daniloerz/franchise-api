package com.franchises.acc.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.franchises.acc.infrastructure.persistence.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findByIdAndBranch_Id(UUID id, UUID branchId);

    @Query("""
            SELECT p FROM ProductEntity p
            WHERE p.branch.franchise.id = :franchiseId
              AND p.stock = (
                  SELECT MAX(p2.stock)
                  FROM ProductEntity p2
                  WHERE p2.branch.id = p.branch.id
              )
            """)
    List<ProductEntity> findTopStockProductsByFranchiseId(@Param("franchiseId") UUID franchiseId);
}

