package com.franchises.acc.domain.service;

import java.time.LocalDateTime;
import java.util.UUID;

import com.franchises.acc.application.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.franchises.acc.application.dto.request.CreateProductRequest;
import com.franchises.acc.application.dto.request.UpdateNameRequest;
import com.franchises.acc.application.dto.request.UpdateStockRequest;
import com.franchises.acc.application.dto.response.ProductResponse;
import com.franchises.acc.domain.exception.ResourceNotFoundException;
import com.franchises.acc.domain.model.Product;
import com.franchises.acc.domain.repository.BranchRepository;
import com.franchises.acc.domain.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponse addProductToBranch(UUID branchId, CreateProductRequest request) {
        branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));

        LocalDateTime now = LocalDateTime.now();
        Product product = Product.builder()
                .name(request.name())
                .stock(request.stock())
                .branchId(branchId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    @Transactional
    public void deleteProduct(UUID branchId, UUID productId) {
        productRepository.findByIdAndBranchId(productId, branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        productRepository.deleteById(productId);
    }

    @Override
    @Transactional
    public ProductResponse updateProductStock(UUID branchId, UUID productId, UpdateStockRequest request) {
        Product product = productRepository.findByIdAndBranchId(productId, branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        product.setStock(request.stock());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProductName(UUID branchId, UUID productId, UpdateNameRequest request) {
        Product product = productRepository.findByIdAndBranchId(productId, branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        product.setName(request.name());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getStock(),
                product.getBranchId(),
                product.getCreatedAt()
        );
    }
}

